package cn.afeibaili.mchat.socket

import cn.afeibaili.mchat.cipher.CipherProcessor
import cn.afeibaili.mchat.config.ServerConfig
import cn.afeibaili.mchat.logger.Logger
import cn.afeibaili.mchat.message.MessageParser
import cn.afeibaili.mchat.message.MessageReader
import cn.afeibaili.mchat.message.MessageType
import java.io.BufferedWriter
import java.io.Closeable
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors


/**
 *  # 服务端结构
 *
 * @author AfeiBaili
 * @version 2026/7/18 04:53
 */

class Server(val config: ServerConfig, val parser: MessageParser) : Closeable {
    private val server = ServerSocket()
    private val cipher = CipherProcessor(config.token)
    private val pool = Executors.newFixedThreadPool(2)
    private val logger = Logger.create("Server")
    private var isAlive = true

    private val clients = mutableSetOf<Socket>()
    private var readers = mutableSetOf<MessageReader>()
    private val removeList = mutableListOf<Socket>()

    private val thread = Thread({
        while (isAlive) {
            runCatching {
                val socket: Socket = server.accept()
                readers.add(MessageReader(socket, cipher, parser))
                clients.add(socket)
            }
        }
    }, "Server").apply { isDaemon = true }

    fun start() {
        runCatching {
            server.soTimeout = 1000
            server.bind(InetSocketAddress(config.port))
            thread.start()
            logger.info("服务器开启成功")
        }.onFailure { logger.error("服务器开启错误: $it") }

        instance = this@Server
    }

    fun send(messageType: MessageType) {
        removeList.clear()
        clients.forEach { socket ->
            runCatching {
                val writer: BufferedWriter = socket.outputStream.bufferedWriter()
                writer.write(cipher.encrypt(messageType.toString()) + "\n")
                writer.flush()
            }.onFailure {
                logger.info("${socket.remoteSocketAddress}断开了连接: ${it.message}")
                removeList.add(socket)
            }
        }
        if (removeList.isNotEmpty()) clients.removeAll(removeList)
    }

    override fun close() {
        server.close()
        pool.shutdown()
        clients.forEach { it.close() }
        readers.forEach { it.close() }
        isAlive = false
        logger.info("服务器已关闭")
    }

    companion object {
        var instance: Server? = null

        init {
            Runtime.getRuntime().addShutdownHook(Thread { instance?.close() })
        }
    }
}