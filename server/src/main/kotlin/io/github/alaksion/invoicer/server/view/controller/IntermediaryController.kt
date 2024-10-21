package io.github.alaksion.invoicer.server.view.controller

import io.github.alaksion.invoicer.server.view.viewmodel.intermediary.CreateIntermediaryResponseViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.intermediary.CreateIntermediaryViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.intermediary.UpdateIntermediaryViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.intermediary.UserIntermediariesViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.intermediary.toModel
import io.github.alaksion.invoicer.server.view.viewmodel.intermediary.toViewModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.impl.intermediary.CreateIntermediaryService
import services.impl.intermediary.DeleteIntermediaryService
import services.impl.intermediary.GetUserIntermediariesService
import services.impl.intermediary.UpdateIntermediaryService
import utils.authentication.api.jwt.jwtProtected
import utils.authentication.api.jwt.jwtUserId
import kotlin.getValue

fun Routing.intermediaryController() {
    route("/intermediary") {
        jwtProtected {
            post {
                val body = call.receive<CreateIntermediaryViewModel>()
                val model = body.toModel()
                val useCase by closestDI().instance<CreateIntermediaryService>()

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
        jwtProtected {
            delete("/{id}") {
                val intermediaryId = call.parameters["id"]!!
                val useCase by closestDI().instance<DeleteIntermediaryService>()

                useCase.execute(
                    beneficiaryId = intermediaryId,
                    userId = jwtUserId()
                )

                call.respond(HttpStatusCode.NoContent)
            }
        }

        jwtProtected {
            get {
                val page = call.request.queryParameters["page"]?.toLongOrNull() ?: 0
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val useCase by closestDI().instance<GetUserIntermediariesService>()

                call.respond(
                    status = HttpStatusCode.OK,
                    message = UserIntermediariesViewModel(
                        intermediaries = useCase.execute(
                            userId = jwtUserId(),
                            page = page,
                            limit = limit
                        ).map { it.toViewModel() }
                    )
                )
            }
        }

        jwtProtected {
            put("/{id}") {
                val intermediaryId = call.parameters["id"]!!
                val useCase by closestDI().instance<UpdateIntermediaryService>()
                val userId = jwtUserId()
                val body = call.receive<UpdateIntermediaryViewModel>()
                call.respond(
                    status = HttpStatusCode.OK,
                    message = useCase.execute(
                        intermediaryId = intermediaryId,
                        model = body.toModel(),
                        userId = userId
                    ).toViewModel()
                )
            }
        }
    }
}