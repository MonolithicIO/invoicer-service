package io.github.alaksion.invoicer.foundation.log

import kotlin.reflect.KClass

interface Logger {
    fun log(
        tag: String,
        message: String,
        level: LogLevel,
        throwable: Throwable? = null
    )

    fun log(
        type: KClass<*>,
        message: String,
        level: LogLevel,
        throwable: Throwable? = null
    )
}

enum class LogLevel {
    Info,
    Debug,
    Warn,
    Error
}