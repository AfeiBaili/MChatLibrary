package cn.afeibaili.mchat.logger

import java.io.PrintWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface Logger {
    val name: String
    fun info(message: Any)
    fun error(message: Any)
    fun warn(message: Any)
    fun print(level: String, message: Any) {
        printer.println("[$level] ${getDataTime()} MChatLogger/$name: $message")
    }

    private fun getDataTime() = LocalDateTime.now().format(formatter)

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss")
        private val printer = PrintWriter(System.out, true)
        fun getLogger(name: Any): Logger {
            return object : Logger {
                override val name: String = name.toString()

                override fun info(message: Any) {
                    print("INFO", message)
                }

                override fun error(message: Any) {
                    print("ERROR", message)
                }

                override fun warn(message: Any) {
                    print("WARN", message)
                }
            }
        }

        fun create(name: Any): Logger = getLogger(name)
    }
}