package io.github.monolithic.invoicer.foundation.log.di

import io.github.monolithic.invoicer.foundation.log.Logger
import io.github.monolithic.invoicer.foundation.log.logback.LogbackLogger
import org.kodein.di.DI
import org.kodein.di.bindProvider

val logDiModule = DI.Module("logDiModule") {
    bindProvider<Logger> { LogbackLogger() }
}
