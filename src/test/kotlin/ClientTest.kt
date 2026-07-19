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
                ClientConfig("qwd#5q&4e", "afeibaili.cn", 33393, "null"), MessageCallback(
                    mutableMapOf(
                        MessageType.Identifiers.Text to { message ->
                            println("server: $message")
                        }
                    )
                ))

            var trues = true
            Thread {
                while (trues) {
                    val readLine: String? = readLine()
                    if (readLine == null) continue
                    if (readLine == "stop") trues = false
                    client.send(MessageType.Text("null", readLine, "null"))
                }
            }.start()

            client.connect()
        }
    }
}