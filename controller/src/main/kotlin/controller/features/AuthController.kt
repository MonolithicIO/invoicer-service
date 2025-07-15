package controller.features

import controller.viewmodel.login.GoogleSignInViewModel
import controller.viewmodel.login.LoginViewModel
import controller.viewmodel.login.RefreshAuthRequestViewModel
import controller.viewmodel.login.toDomainModel
import controller.viewmodel.login.toViewModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import io.github.alaksion.invoicer.services.login.GoogleLoginService
import io.github.alaksion.invoicer.services.login.LoginService
import io.github.alaksion.invoicer.services.login.RefreshLoginService
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
            val body = call.receive<RefreshAuthRequestViewModel>()
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
