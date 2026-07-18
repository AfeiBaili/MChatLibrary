import cn.afeibaili.mchat.config.ClientConfig
import cn.afeibaili.mchat.message.MessageParser
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
                ClientConfig("hello", "localhost", 33393, "null"), MessageParser(
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
                    client.send(MessageType.Text("null", "0", readLine ?: ""))
                }
            }.start()


            client.connect()
            client.close()
            trues = false
        }
    }
}