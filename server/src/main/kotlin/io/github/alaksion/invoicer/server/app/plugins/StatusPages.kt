package io.github.alaksion.invoicer.server.app.plugins

import io.github.alaksion.invoicer.server.errors.ErrorBody
import io.github.alaksion.invoicer.server.errors.HttpError
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

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