package io.github.alaksion.invoicer.server.app.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import java.time.format.DateTimeParseException
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import utils.exceptions.InvalidUUIDException
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError

fun Application.installStatusPages(
    clock: Clock
) {

    install(StatusPages) {
        exception<HttpError> { call, cause ->
            call.respond(
                message = ErrorBody(message = cause.message, timeStamp = clock.now()),
                status = cause.statusCode.toKtorCode()
            )
        }

        exception<InvalidUUIDException> { call, cause ->
            call.respond(
                message = ErrorBody(
                    message = cause.message.orEmpty(),
                    timeStamp = clock.now()
                ),
                status = HttpStatusCode.BadRequest
            )
        }
        exception<DateTimeParseException> { call, _ ->
            call.respond(
                message = ErrorBody(message = "Invalid date format", timeStamp = clock.now()),
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
    val timeStamp: Instant
)
