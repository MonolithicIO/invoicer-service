package controller.features

import controller.viewmodel.createuser.CreateUserRequestViewModel
import controller.viewmodel.createuser.CreateUserResponseViewModel
import controller.viewmodel.createuser.toDomainModel
import io.github.alaksion.invoicer.foundation.authentication.token.jwt.jwtProtected
import io.github.alaksion.invoicer.foundation.authentication.token.jwt.jwtUserId
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.user.CreateUserService
import services.api.services.user.DeleteUserService

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
                useCase.delete(parseUuid(jwtUserId()))
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}