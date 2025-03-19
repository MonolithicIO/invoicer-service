package services.fakes

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * Fake implementation of [Clock] for testing purposes. [nowResponse] defaults to my birthday: 06/19/2000 00:00:00
 */

internal class FakeClock : Clock {

    var nowResponse = Instant.parse("2000-06-19T00:00:00Z")

    override fun now(): Instant = nowResponse
}