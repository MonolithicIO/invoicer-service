package invoicer.alaksiondev.com.app.plugins

import invoicer.alaksiondev.com.errors.ErrorBody
import invoicer.alaksiondev.com.errors.HttpError
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