package io.github.alaksion.invoicer.server.app.plugins

import utils.exceptions.http.HttpCode
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import utils.exceptions.http.HttpError
import utils.exceptions.InvalidUUIDException
import java.time.format.DateTimeParseException

fun Application.installStatusPages() {
    install(StatusPages) {
        exception<HttpError> { call, cause ->
            call.respond(
                message = ErrorBody(cause.message),
                status = cause.statusCode.toKtorCode()
            )
        }

        exception<InvalidUUIDException> { call, cause ->
            call.respond(
                message = ErrorBody(cause.message.orEmpty()),
                status = HttpStatusCode.BadRequest
            )
        }
        exception<DateTimeParseException> { call, _ ->
            call.respond(
                message = ErrorBody("Invalid date format"),
                status = HttpStatusCode.BadRequest
            )
        }
    }
}

private fun HttpCode.toKtorCode(): HttpStatusCode = when (this) {
    HttpCode.BadRequest -> HttpStatusCode.BadRequest
    HttpCode.Forbidden -> HttpStatusCode.Forbidden
    HttpCode.NotFound -> HttpStatusCode.NotFound
    HttpCode.Conflict -> HttpStatusCode.Conflict
    HttpCode.UnAuthorized -> HttpStatusCode.Unauthorized
    HttpCode.Gone -> HttpStatusCode.Gone
    HttpCode.ServerError -> HttpStatusCode.InternalServerError
}

@Serializable
private data class ErrorBody(
    val message: String,
) {
    val timeStamp = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
}
