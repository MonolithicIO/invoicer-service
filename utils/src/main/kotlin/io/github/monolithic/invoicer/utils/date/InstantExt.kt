package io.github.monolithic.invoicer.utils.date

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun Instant.toLocalDate(timeZone: TimeZone = AppTimeZone): LocalDate {
    return this.toLocalDateTime(timeZone).date
}

fun Instant.toFormattedDateTime(): String {
    val localDateTime = this.toLocalDateTime(AppTimeZone)
    val year = localDateTime.year.toString()
    val month = localDateTime.monthNumber.toString().padStart(2, '0')
    val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
    val hour = localDateTime.hour.toString().padStart(2, '0')
    val minute = localDateTime.minute.toString().padStart(2, '0')
    val second = localDateTime.second.toString().padStart(2, '0')

    return buildString {
        append(month)
        append("-")
        append(day)
        append("-")
        append(year)
        append(" ")
        append(hour)
        append(":")
        append(minute)
        append(":")
        append(second)
        append(" ")
        append(AppTimeZone.id)
    }
}
