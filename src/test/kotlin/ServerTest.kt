import cn.afeibaili.mchat.config.ServerConfig
import cn.afeibaili.mchat.message.MessageCallback
import cn.afeibaili.mchat.message.MessageType
import cn.afeibaili.mchat.socket.Server

/**
 *
 *
 * @author AfeiBaili
 * @version 2026/7/18 05:48
 */

class ServerTest {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val server = Server(ServerConfig("a", 33393), callbacks = MessageCallback(mapOf()), onVerify = {
                println(it)
            })

            server.start()

            var isActive = true
            while (isActive) {
                val readLine: String? = readLine()
                if (readLine == null) continue
                if (readLine == "stop") isActive = false
                server.sendAll(MessageType.Text("console", readLine))
            }
        }
    }
}