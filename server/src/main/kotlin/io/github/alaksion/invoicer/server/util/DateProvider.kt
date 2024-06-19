package io.github.alaksion.invoicer.server.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


interface DateProvider {
    fun now(): LocalDate
}

object DateProviderImplementation : DateProvider {
    override fun now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}