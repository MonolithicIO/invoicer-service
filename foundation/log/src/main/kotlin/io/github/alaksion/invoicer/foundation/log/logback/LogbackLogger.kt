package io.github.alaksion.invoicer.foundation.log.logback

import io.github.alaksion.invoicer.foundation.log.LogLevel
import io.github.alaksion.invoicer.foundation.log.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

internal class LogbackLogger : Logger {

    override fun log(tag: String, message: String, level: LogLevel, throwable: Throwable?) {
        val logger = LoggerFactory.getLogger(tag)

        when (level) {
            LogLevel.Info -> logger.info(message)
            LogLevel.Debug -> logger.debug(message)
            LogLevel.Warn -> logger.warn(message)
            LogLevel.Error -> logger.error(message)
        }
    }

    override fun log(type: KClass<*>, message: String, level: LogLevel, throwable: Throwable?) {
        val logger = LoggerFactory.getLogger(type.java)

        when (level) {
            LogLevel.Info -> logger.info(message, throwable)
            LogLevel.Debug -> logger.debug(message, throwable)
            LogLevel.Warn -> logger.warn(message, throwable)
            LogLevel.Error -> logger.error(message, throwable)
        }
    }
}