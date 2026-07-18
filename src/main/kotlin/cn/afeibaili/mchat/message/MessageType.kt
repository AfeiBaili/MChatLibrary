package cn.afeibaili.mchat.message

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

/**
 * # 消息类
 *
 * @author AfeiBaili
 * @version 2026/7/17 19:55
 */

sealed class MessageType {
    abstract val source: String
    abstract val channel: String
    abstract val content: String
    @get:JsonIgnore
    abstract val identifier: String

    enum class Identifiers(val value: String) {
        Text("text"),
        Command("command"),
        Heartbeat("heartbeat"),
        Image("image"),
    }

    class Text(
        override val source: String,
        override val channel: String,
        override val content: String,
    ) : MessageType() {
        override val identifier get() = Identifiers.Text.value
    }

    class Command(
        override val source: String,
        override val channel: String,
        override val content: String,
    ) : MessageType() {
        override val identifier get() = Identifiers.Command.value
    }

    class Heartbeat(
        override val source: String = "",
        override val channel: String = "",
        override val content: String = "",
    ) : MessageType() {
        override val identifier get() = Identifiers.Heartbeat.value
        override fun toString(): String = identifier
    }

    class Image(
        override val source: String,
        override val channel: String,
        override val content: String,
    ) : MessageType() {
        override val identifier get() = Identifiers.Image.value
    }

    override fun toString(): String = identifier + messageJsonMapper.writeValueAsString(this)

    companion object {
        val messageJsonMapper: ObjectMapper = ObjectMapper().registerModule(KotlinModule.Builder().build())
        inline fun <reified Type : MessageType> formJson(json: String): Type? {
            return runCatching {
                messageJsonMapper.readValue<Type>(json, Type::class.java)
            }.getOrElse {
                println(it)
                return null
            }
        }
    }
}