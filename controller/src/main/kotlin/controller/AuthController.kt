package controller

import controller.viewmodel.login.LoginResponseViewModel
import controller.viewmodel.login.LoginViewModel
import controller.viewmodel.login.toDomainModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.login.LoginService

internal fun Routing.authController() {
    route("auth") {
        post("/login") {
            val body = call.receive<LoginViewModel>()
            val model = body.toDomainModel()
            val loginUseCase by closestDI().instance<LoginService>()

            call.respond(
                message = LoginResponseViewModel(
                    token = loginUseCase.login(model)
                ),
                status = HttpStatusCode.OK
            )
        }
    }
}