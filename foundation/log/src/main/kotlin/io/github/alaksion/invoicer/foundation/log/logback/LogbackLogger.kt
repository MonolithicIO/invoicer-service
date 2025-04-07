package io.github.alaksion.invoicer.foundation.log.logback

import io.github.alaksion.invoicer.foundation.log.LogLevel
import io.github.alaksion.invoicer.foundation.log.Logger
import org.slf4j.LoggerFactory

internal class LogbackLogger: Logger {

    override fun log(tag: String, message: String, level: LogLevel) {
        val logger = LoggerFactory.getLogger(tag)

        when(level) {
            LogLevel.Info -> logger.info(message)
            LogLevel.Debug -> logger.debug(message)
            LogLevel.Warn -> logger.warn(message)
            LogLevel.Error -> logger.error(message)
        }
    }
}