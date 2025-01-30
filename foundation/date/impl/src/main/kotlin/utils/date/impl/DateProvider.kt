package utils.date.impl

import kotlinx.datetime.*


interface DateProvider {
    fun now(): LocalDate
    fun currentInstant(): Instant
}

object DateProviderImplementation : DateProvider {
    override fun now() = currentInstant().toLocalDateTime(TimeZone.currentSystemDefault()).date

    override fun currentInstant(): Instant = Clock.System.now()
}