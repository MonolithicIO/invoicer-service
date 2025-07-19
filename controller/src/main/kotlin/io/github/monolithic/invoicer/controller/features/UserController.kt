package io.github.monolithic.invoicer.controller.features

import io.github.monolithic.invoicer.controller.viewmodel.createuser.CreateUserRequestViewModel
import io.github.monolithic.invoicer.controller.viewmodel.createuser.CreateUserResponseViewModel
import io.github.monolithic.invoicer.controller.viewmodel.createuser.toDomainModel
import io.github.monolithic.invoicer.foundation.authentication.token.jwt.jwtProtected
import io.github.monolithic.invoicer.foundation.authentication.token.jwt.jwtUserId
import io.github.monolithic.invoicer.services.user.CreateUserService
import io.github.monolithic.invoicer.services.user.DeleteUserService
import io.github.monolithic.invoicer.utils.uuid.parseUuid
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

internal fun Routing.userController() {
    route("/v1/user") {
        createUser()
        deleteUser()
    }
}

private fun Route.createUser() = post {
    val body = call.receive<CreateUserRequestViewModel>()
    val parsed = body.toDomainModel()
    val useCase by closestDI().instance<CreateUserService>()
    call.respond(
        message = CreateUserResponseViewModel(useCase.create(parsed)),
        status = HttpStatusCode.Created
    )
}

private fun Route.deleteUser() = jwtProtected {
    delete {
        val useCase by closestDI().instance<DeleteUserService>()
        useCase.delete(parseUuid(jwtUserId()))
        call.respond(HttpStatusCode.NoContent)
    }
}
