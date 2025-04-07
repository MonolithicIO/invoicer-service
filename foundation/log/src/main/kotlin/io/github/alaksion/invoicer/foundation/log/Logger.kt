package io.github.alaksion.invoicer.foundation.log

interface Logger {
    fun log(
        tag: String,
        message: String,
        level: LogLevel
    )
}

enum class LogLevel {
    Info,
    Debug,
    Warn,
    Error
}