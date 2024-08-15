package io.github.alaksion.invoicer.server.view.controller

import io.github.alaksion.invoicer.server.domain.usecase.intermediary.CreateIntermediaryUseCase
import io.github.alaksion.invoicer.server.view.viewmodel.intermediary.CreateIntermediaryResponseViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.intermediary.CreateIntermediaryViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.intermediary.toModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import utils.authentication.api.jwt.jwtProtected
import utils.authentication.api.jwt.jwtUserId

fun Routing.intermediaryController() {
    route("/intermediary") {
        jwtProtected {
            post {
                val body = call.receive<CreateIntermediaryViewModel>()
                val model = body.toModel()
                val useCase by closestDI().instance<CreateIntermediaryUseCase>()

                call.respond(
                    message =
                    CreateIntermediaryResponseViewModel(
                        id = useCase.create(
                            model = model,
                            userId = jwtUserId()
                        )
                    ),
                    status = HttpStatusCode.Created
                )
            }
        }
    }
}