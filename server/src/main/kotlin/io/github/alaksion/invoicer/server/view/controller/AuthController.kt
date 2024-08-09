package io.github.alaksion.invoicer.server.view.controller

import io.github.alaksion.invoicer.server.domain.usecase.login.LoginUseCase
import io.github.alaksion.invoicer.server.view.viewmodel.login.LoginResponseViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.login.LoginViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.login.toDomainModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Routing.authController() {
    route("auth") {
        post("/login") {
            val body = call.receive<LoginViewModel>()
            val model = body.toDomainModel()
            val loginUseCase by closestDI().instance<LoginUseCase>()

            call.respond(
                message = LoginResponseViewModel(
                    token = loginUseCase.login(model)
                ),
                status = HttpStatusCode.OK
            )
        }
    }
}