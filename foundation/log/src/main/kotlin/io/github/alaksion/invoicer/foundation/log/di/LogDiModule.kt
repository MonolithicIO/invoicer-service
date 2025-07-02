package io.github.alaksion.invoicer.foundation.log.di

import io.github.alaksion.invoicer.foundation.log.Logger
import io.github.alaksion.invoicer.foundation.log.logback.LogbackLogger
import org.kodein.di.DI
import org.kodein.di.bindProvider

val logDiModule = DI.Module("logDiModule") {
    bindProvider<Logger> { LogbackLogger() }
}
