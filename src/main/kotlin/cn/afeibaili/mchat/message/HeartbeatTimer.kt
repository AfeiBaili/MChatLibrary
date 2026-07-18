package cn.afeibaili.mchat.message

import java.io.Closeable
import java.io.PrintWriter
import java.net.Socket


/**
 * # 心跳定时器
 *
 * @author AfeiBaili
 * @version 2026/7/18 04:48
 */

class HeartbeatTimer(val millis: Long, socket: Socket) : Closeable {
    private val writer = PrintWriter(socket.getOutputStream())
    private var isActive = true
    private val thread = Thread({
        while (isActive) {
            Thread.sleep(millis)
            writer.println(MessageType.Heartbeat())
        }
    }, "HeartbeatTimer").apply { isDaemon = true }

    init {
        thread.start()
    }

    override fun close() {
        isActive = false
        writer.close()
    }
}