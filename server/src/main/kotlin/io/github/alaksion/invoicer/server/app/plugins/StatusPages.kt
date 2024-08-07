package io.github.alaksion.invoicer.server.app.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import utils.exceptions.ErrorBody
import utils.exceptions.HttpError

fun Application.installStatusPages() {
    install(StatusPages) {
        exception<HttpError> { call, cause ->
            call.respond(
                message = ErrorBody(cause.message),
                status = cause.statusCode
            )
        }
    }
}