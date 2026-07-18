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

class MessageReader(socket: Socket, val cipher: CipherProcessor, val parser: MessageParser) : Closeable {
    private var isActive = true
    private val reader = socket.inputStream.bufferedReader()
    private val logger = Logger.Companion.create("MessageReader")
    private val thread = Thread({
        runCatching {
            var line = ""
            while (isActive && reader.readLine().also { line = it } != null) {
                val message: String = cipher.decrypt(line)
                paseMessage(message)
            }
            logger.info("读取流已达结尾")
        }
    }, "MessageReader").apply { isDaemon = true }

    init {
        thread.start()
    }

    fun paseMessage(message: String) {
        when {
            message.startsWith(MessageType.Identifiers.Text.value) -> {
                matchMessageType<MessageType.Text>(
                    MessageType.Identifiers.Text,
                    message.removePrefix(MessageType.Identifiers.Text.value)
                )
            }

            message.startsWith(MessageType.Identifiers.Command.value) -> {
                matchMessageType<MessageType.Command>(
                    MessageType.Identifiers.Command,
                    message.removePrefix(MessageType.Identifiers.Command.value)
                )
            }

            message.startsWith(MessageType.Identifiers.Image.value) -> {
                matchMessageType<MessageType.Image>(
                    MessageType.Identifiers.Image,
                    message.removePrefix(MessageType.Identifiers.Image.value)
                )
            }

            message.startsWith(MessageType.Identifiers.Heartbeat.value) -> {}
        }
    }


    private inline fun <reified Type : MessageType> matchMessageType(
        type: MessageType.Identifiers,
        jsonMessage: String,
    ) {

        println(jsonMessage)

        val fn: ((MessageType) -> Unit)? = parser.messageMap[type]
        fn ?: return
        fn(MessageType.formJson<Type>(jsonMessage) ?: return)
    }

    override fun close() {
        isActive = false
        reader.close()
    }
}