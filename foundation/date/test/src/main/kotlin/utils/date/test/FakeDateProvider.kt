package utils.date.test

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import utils.date.api.DateProvider

class FakeDateProvider : DateProvider {

    var nowResponse: () -> LocalDate = { LocalDate(year = 2000, dayOfMonth = 19, monthNumber = 6) }
    var nowDateTimeResponse: () -> LocalDateTime = {
        LocalDateTime(
            year = 2000,
            monthNumber = 6,
            dayOfMonth = 19,
            hour = 0,
            minute = 0,
            second = 0
        )
    }
    var nowInstant = Instant.parse("2000-06-19T00:00:00Z")


    override fun now(): LocalDate {
        return nowResponse()
    }

    override fun currentInstant(): Instant = nowInstant

}