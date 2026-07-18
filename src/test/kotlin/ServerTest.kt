import cn.afeibaili.mchat.message.MessageParser
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
            val server = Server(
                33393, "hello", MessageParser(
                    mutableMapOf(
                        MessageType.Identifiers.Text to { message ->
                            println("client: $message")
                        }
                    )
                )
            )
        }
    }
}