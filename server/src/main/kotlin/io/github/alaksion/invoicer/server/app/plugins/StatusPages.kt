package io.github.alaksion.invoicer.server.app.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import utils.exceptions.ErrorBody
import utils.exceptions.HttpError
import java.time.format.DateTimeParseException

fun Application.installStatusPages() {
    install(StatusPages) {
        exception<HttpError> { call, cause ->
            call.respond(
                message = ErrorBody(cause.message),
                status = cause.statusCode
            )

        }
        exception<DateTimeParseException> { call, cause ->
            call.respond(
                message = ErrorBody("Invalid date format"),
                status = HttpStatusCode.BadRequest
            )
        }
    }
}