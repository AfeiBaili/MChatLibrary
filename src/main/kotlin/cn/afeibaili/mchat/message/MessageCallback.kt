package cn.afeibaili.mchat.message


/**
 * # 消息解析器
 *
 * @author AfeiBaili
 * @version 2026/7/17 21:31
 */

class MessageCallback(
    val callbacks: Map<MessageType.Identifiers, (MessageType) -> Unit>,
)