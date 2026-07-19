import cn.afeibaili.mchat.config.ClientConfig
import cn.afeibaili.mchat.message.MessageCallback
import cn.afeibaili.mchat.message.MessageType
import cn.afeibaili.mchat.socket.Client

/**
 *
 *
 * @author AfeiBaili
 * @version 2026/7/18 05:48
 */

class ClientTest {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val client = Client(
                ClientConfig("a", "localhost", 33393, "client"), MessageCallback(
                    mutableMapOf(
                        MessageType.Identifiers.Text to { message ->
                            println("server: $message")
                        }
                    )
                ))

            var isActive = true
            Thread {
                while (isActive) {
                    val readLine: String? = readLine()
                    if (readLine == null) continue
                    if (readLine == "stop") isActive = false
                    client.send(MessageType.Text("null", readLine, "null"))
                }
            }.start()

            client.connect()
        }
    }
}