package cn.afeibaili.mchat.message

import cn.afeibaili.mchat.cipher.CipherProcessor
import cn.afeibaili.mchat.logger.Logger
import java.io.Closeable
import java.net.Socket

/**
 * # 消息读取器
 *
 * @author AfeiBaili
 * @version 2026/7/17 20:33
 */

class MessageReader(socket: Socket, val cipher: CipherProcessor, val messageCallback: MessageCallback) : Closeable {
    private var isActive = true
    private val reader = socket.inputStream.bufferedReader()
    private val logger = Logger.Companion.create("MessageReader")
    private val thread = Thread({
        runCatching {
            var line = ""
            while (isActive && reader.readLine().also { line = it } != null) {
                runCatching {
                    val message: String = cipher.decrypt(line)
                    paseMessage(message)
                }
            }
            logger.info("读取流已达结尾")
        }.onFailure { "读取流已关闭连接" }
    }, "MessageReader").apply { isDaemon = true }

    init {
        thread.start()
    }

    fun paseMessage(message: String) {
        when (MessageType.findIdentifier(message)) {
            MessageType.Identifiers.Text.value -> {
                matchMessageType(MessageType.Identifiers.Text, message)
            }

            MessageType.Identifiers.Command.value -> {
                matchMessageType(MessageType.Identifiers.Command, message)
            }

            MessageType.Identifiers.Image.value -> {
                matchMessageType(MessageType.Identifiers.Image, message)
            }

            MessageType.Identifiers.Heartbeat.value -> {}
        }
    }


    private fun matchMessageType(
        type: MessageType.Identifiers,
        messageString: String,
    ) {
        println(messageString)
        val fn: ((MessageType) -> Unit)? = messageCallback.callbacks[type]
        fn ?: return
        fn(MessageType.fromString(messageString) ?: return)
    }

    override fun close() {
        isActive = false
        reader.close()
    }
}