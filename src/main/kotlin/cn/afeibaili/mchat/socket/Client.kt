package cn.afeibaili.mchat.socket

import cn.afeibaili.mchat.cipher.CipherProcessor
import cn.afeibaili.mchat.config.ClientConfig
import cn.afeibaili.mchat.logger.Logger
import cn.afeibaili.mchat.message.HeartbeatTimer
import cn.afeibaili.mchat.message.MessageParser
import cn.afeibaili.mchat.message.MessageReader
import cn.afeibaili.mchat.message.MessageType
import java.io.Closeable
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Executors


/**
 * # 客户端类
 *
 * @author AfeiBaili
 * @version 2026/7/17 20:11
 */

class Client(val config: ClientConfig, val parser: MessageParser) : Closeable {
    private val cipher = CipherProcessor(config.token)
    private val socket = Socket()
    private val logger = Logger.create("Client")
    private val pool = Executors.newFixedThreadPool(2)
    private var writer: PrintWriter? = null
    private var reader: MessageReader? = null
    private var heartbeat: HeartbeatTimer? = null

    private val thread = Thread({
        runCatching {
            socket.connect(InetSocketAddress(config.host, config.port), 5 * 1000)
            writer = PrintWriter(socket.getOutputStream(), true)
            reader = MessageReader(socket, cipher, parser)
            heartbeat = HeartbeatTimer(1000 * 60 * 5, socket)
            logger.info("已建立连接")
        }.onFailure {
            logger.error("无法连接至服务器: ${it.message}")
        }
        instance = this@Client

    }, "MChatSystem").apply { isDaemon = true }

    fun connect() {
        thread.start()
        logger.info("客户端已启动")
    }


    fun send(message: MessageType) {
        runCatching {
            pool.execute {
                runCatching {
                    writer?.println(cipher.encrypt(message.toString()))
                }.onFailure { logger.warn("无法发送消息至${this.config.host}: ${it.message}") }
            }
        }.onFailure { logger.warn("线程池已关闭") }
    }

    override fun close() {
        socket.close()
        reader?.close()
        writer?.close()
        heartbeat?.close()
        pool.shutdown()
        logger.info("已关闭客户端")
    }

    companion object {
        var instance: Client? = null

        init {
            Runtime.getRuntime().addShutdownHook(Thread { instance?.close() })
        }
    }
}