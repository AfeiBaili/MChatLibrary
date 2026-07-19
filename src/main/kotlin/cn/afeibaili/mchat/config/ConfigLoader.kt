package cn.afeibaili.mchat.config

import java.io.File


/**
 * # 配置文件加载器
 *
 * @author AfeiBaili
 * @version 2026/7/19 15:19
 */

object ConfigLoader {
    val path = "${System.getProperty("user.dir")}/config"
    private val regex = "^\\s*\\[.*]\\s*$".toRegex()
    private val valueRegex = "^\\s*(.*?)\\s*=\\s*(.*?)\\s*$".toRegex()

    fun loadClient(): ClientConfig {
        val file = File(path, "mchat.config")
        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
            file.writeText(buildString {
                appendLine("[ 通信密钥 ]")
                appendLine("token = null")
                appendLine("[ 主机地址（不带端口号） ]")
                appendLine("host = localhost")
                appendLine("[ 端口号 ]")
                appendLine("port = 33393")
                appendLine("[ 服务器名称 ]")
                appendLine("[ 请使用\"emoji+名称\"的命名方式 ]")
                appendLine("[ 根据颜色特点可方便区分不同服务器 ]")
                appendLine("name = 😀服务器")
            })
        }
        val lines: List<String> = file.readLines()

        var token = ""
        var host = ""
        var port = 0
        var name = ""
        lines.forEach { line ->
            if (regex.matches(line)) return@forEach
            runCatching {
                val key: String = valueRegex.find(line)!!.groups[1]!!.value
                val value: String = valueRegex.find(line)!!.groups[2]!!.value

                when (key) {
                    "token" -> token = value
                    "host" -> host = value
                    "port" -> port = value.toInt()
                    "name" -> name = value
                }
            }.onFailure { throw IllegalStateException("无法解析的配置文件行: $line; $it") }
        }

        if (token.isEmpty() || host.isEmpty() || port == 0 || name.isEmpty())
            throw IllegalArgumentException("请检查配置文件是否合规: token=$token, host=$host, port=$port, name=$name")

        return ClientConfig(token, host, port, name)
    }
}