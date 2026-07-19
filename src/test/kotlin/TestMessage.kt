import cn.afeibaili.mchat.cipher.CipherProcessor
import cn.afeibaili.mchat.message.MessageType
import kotlin.test.Test

/**
 * 消息测试
 *
 * @author AfeiBaili
 * @version 2026/7/19 11:31
 */

class TestMessage {
    @Test
    fun test01() {
        val text = MessageType.Command("source:{dad}", "source:{a}")
        val cipher = CipherProcessor("xxxx")
        val cipher2 = CipherProcessor("aad")

        val encrypt: String = cipher.encrypt(text.toString())
        println(encrypt)
        println(MessageType.fromString(text.toString()))
        println(cipher.decrypt(encrypt))
    }

    @Test
    fun testRegex() {
        val valueRegex = "^\\s*(.*?)\\s*=\\s*(.*?)\\s*$".toRegex()

        val groups: MatchGroupCollection = valueRegex.find("ad=dqd")!!.groups
        println("[${groups[1]!!.value}]")
        println("[${groups[2]!!.value}]")
    }
}