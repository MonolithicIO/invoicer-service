package controller

import controller.viewmodel.login.LoginViewModel
import controller.viewmodel.login.RefreshAuthRequest
import controller.viewmodel.login.toDomainModel
import controller.viewmodel.login.toViewModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.login.LoginService
import services.api.services.login.RefreshLoginService
import utils.exceptions.unauthorizedResourceError

internal fun Routing.authController() {
    route("/v1/auth") {
        post("/login") {
            val body = call.receive<LoginViewModel>()
            val model = body.toDomainModel()
            val loginService by closestDI().instance<LoginService>()

            call.respond(
                message = loginService.login(model).toViewModel(),
                status = HttpStatusCode.OK
            )
        }

        post("/refresh") {
            val body = call.receive<RefreshAuthRequest>()
            val refreshService by closestDI().instance<RefreshLoginService>()

            call.respond(
                message = refreshService.refreshLogin(
                    refreshToken = body.refreshToken ?: unauthorizedResourceError(),
                ).toViewModel(),
            )
        }
    }
}