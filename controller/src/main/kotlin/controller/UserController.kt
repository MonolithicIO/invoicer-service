package controller

import controller.viewmodel.createuser.CreateUserRequestViewModel
import controller.viewmodel.createuser.CreateUserResponseViewModel
import controller.viewmodel.createuser.toDomainModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.user.CreateUserService
import services.api.services.user.DeleteUserService
import foundation.authentication.impl.jwt.jwtProtected
import foundation.authentication.impl.jwt.jwtUserId

internal fun Routing.userController() {
    route("/v1/user") {
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