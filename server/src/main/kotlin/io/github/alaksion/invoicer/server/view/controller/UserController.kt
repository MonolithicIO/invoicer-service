package io.github.alaksion.invoicer.server.view.controller

import io.github.alaksion.invoicer.server.view.viewmodel.createuser.CreateUserRequestViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.createuser.CreateUserResponseViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.createuser.toDomainModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.user.CreateUserService
import services.api.services.user.DeleteUserService
import utils.authentication.api.jwt.jwtProtected
import utils.authentication.api.jwt.jwtUserId

fun Routing.userController() {
    route("user") {
        post {
            val body = call.receive<CreateUserRequestViewModel>()
            val parsed = body.toDomainModel()
            val useCase by closestDI().instance<CreateUserService>()
            call.respond(
                message = CreateUserResponseViewModel(useCase.create(parsed)),
                status = HttpStatusCode.Created
            )
        }

        jwtProtected {
            delete {
                val useCase by closestDI().instance<DeleteUserService>()
                useCase.delete(jwtUserId())
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}