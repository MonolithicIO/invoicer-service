package domains.user.controller

import domains.user.controller.viewmodel.createuser.CreateUserRequestViewModel
import domains.user.controller.viewmodel.createuser.CreateUserResponseViewModel
import domains.user.controller.viewmodel.createuser.toDomainModel
import domains.user.domain.api.usecase.CreateUserUseCase
import domains.user.domain.api.usecase.DeleteUserUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import utils.authentication.api.jwt.jwtProtected
import utils.authentication.api.jwt.jwtUserId
import kotlin.getValue

fun Routing.userController() {
    route("user") {
        post {
            val body = call.receive<CreateUserRequestViewModel>()
            val parsed = body.toDomainModel()
            val useCase by closestDI().instance<CreateUserUseCase>()
            call.respond(
                message = CreateUserResponseViewModel(useCase.create(parsed)),
                status = HttpStatusCode.Created
            )
        }

        jwtProtected {
            delete {
                val useCase by closestDI().instance<DeleteUserUseCase>()
                useCase.delete(jwtUserId())
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}