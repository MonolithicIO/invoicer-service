package controller.features

import controller.viewmodel.login.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.login.GoogleLoginService
import services.api.services.login.LoginService
import services.api.services.login.RefreshLoginService
import utils.exceptions.http.badRequestError
import utils.exceptions.http.forbiddenError

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
                    refreshToken = body.refreshToken ?: forbiddenError(),
                ).toViewModel(),
            )
        }

        post("/google") {
            val body = call.receive<GoogleSignInViewModel>()
            val googleLoginService by closestDI().instance<GoogleLoginService>()

            call.respond(
                status = HttpStatusCode.OK,
                message = googleLoginService.login(
                    token = body.token ?: badRequestError("Token is required")
                ).toViewModel()
            )
        }
    }
}