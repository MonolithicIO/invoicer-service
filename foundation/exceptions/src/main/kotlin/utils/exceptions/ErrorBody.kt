package utils.exceptions

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class ErrorBody(
    val message: String,
) {
    val timeStamp = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}
