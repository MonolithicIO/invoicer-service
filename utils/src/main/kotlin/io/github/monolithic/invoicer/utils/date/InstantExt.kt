package io.github.monolithic.invoicer.utils.date

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.toLocalDate(timeZone: TimeZone = AppTimeZone): LocalDate {
    return this.toLocalDateTime(timeZone).date
}
