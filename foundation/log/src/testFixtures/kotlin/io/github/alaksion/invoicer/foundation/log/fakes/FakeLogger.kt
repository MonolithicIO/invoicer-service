package io.github.alaksion.invoicer.foundation.log.fakes

import io.github.alaksion.invoicer.foundation.log.LogLevel
import io.github.alaksion.invoicer.foundation.log.Logger
import kotlin.reflect.KClass

class FakeLogger : Logger {

    var logCalls = 0
        private set

    var logTypeCalls = 0
        private set

    override fun log(
        tag: String,
        message: String,
        level: LogLevel,
        throwable: Throwable?
    ) {
        logCalls++
    }

    override fun log(
        type: KClass<*>,
        message: String,
        level: LogLevel,
        throwable: Throwable?
    ) {
        logTypeCalls++
    }
}