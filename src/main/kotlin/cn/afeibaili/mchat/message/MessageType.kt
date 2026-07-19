package cn.afeibaili.mchat.message

/**
 * # 消息类
 *
 * @author AfeiBaili
 * @version 2026/7/17 19:55
 */

sealed class MessageType {
    abstract val source: String
    abstract val content: String
    abstract val channel: String
    abstract val identifier: String

    enum class Identifiers(val value: String) {
        Text("text"),
        Command("command"),
        Image("image"),
        Heartbeat("heartbeat"),
        Verify("verify"),
    }

    class Text(
        override val source: String,
        override val content: String,
        override val channel: String = "",
    ) : MessageType() {
        override val identifier get() = Identifiers.Text.value
    }

    class Command(
        override val source: String,
        override val content: String,
        override val channel: String = "",
    ) : MessageType() {
        override val identifier get() = Identifiers.Command.value
    }

    class Image(
        override val source: String,
        override val content: String,
        override val channel: String = "",
    ) : MessageType() {
        override val identifier get() = Identifiers.Image.value
    }

    class Heartbeat(
        override val source: String = "",
        override val content: String = "",
        override val channel: String = "",
    ) : MessageType() {
        override val identifier get() = Identifiers.Heartbeat.value
    }

    class Verify(
        override val source: String,
        override val content: String = "",
        override val channel: String = "",
    ) : MessageType() {
        override val identifier get() = Identifiers.Verify.value
    }

    override fun toString(): String =
        "identifier:{${this.identifier}};source:{${this.source}};content:{${this.content}};channel:{${this.channel}}"

    companion object {
        val regex = "identifier:\\{(.*?)};source:\\{(.*?)};content:\\{(.*?)};channel:\\{(.*?)}".toRegex()
        fun findIdentifier(string: String) = regex.find(string)?.groups[1]?.value

        fun fromString(string: String): MessageType? {
            val entire: MatchResult? = regex.find(string)
            entire!!
            val identifier: String = entire.groups[1]?.value ?: return null
            val source: String = entire.groups[2]?.value ?: ""
            val content: String = entire.groups[3]?.value ?: ""
            val channel: String = entire.groups[4]?.value ?: ""

            return when (identifier) {
                Identifiers.Text.value -> Text(source, content, channel)
                Identifiers.Command.value -> Command(source, content, channel)
                Identifiers.Image.value -> Image(source, content, channel)
                Identifiers.Heartbeat.value -> Heartbeat(source, content, channel)
                Identifiers.Verify.value -> Image(source, content, channel)
                else -> return null
            }
        }
    }
}