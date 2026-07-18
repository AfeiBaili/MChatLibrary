package cn.afeibaili.mchat.config


/**
 * # 配置类
 *
 * @author AfeiBaili
 * @version 2026/7/17 19:39
 */

open class ClientConfig(
    open val token: String,
    open val host: String,
    open val port: Int,
    open val name: String,
)