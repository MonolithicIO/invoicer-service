package controller

import controller.viewmodel.intermediary.*
import foundation.authentication.impl.jwt.jwtProtected
import foundation.authentication.impl.jwt.jwtUserId
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.intermediary.*

internal fun Routing.intermediaryController() {
    route("/v1/intermediary") {
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
                                userId = parseUuid(jwtUserId())
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
                    beneficiaryId = parseUuid(intermediaryId),
                    userId = parseUuid(jwtUserId())
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
                            userId = parseUuid(jwtUserId()),
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
                val body = call.receive<UpdateIntermediaryViewModel>()
                call.respond(
                    status = HttpStatusCode.OK,
                    message = useCase.execute(
                        intermediaryId = parseUuid(intermediaryId),
                        model = body.toModel(),
                        userId = parseUuid(jwtUserId())
                    ).toViewModel()
                )
            }
        }

        jwtProtected {
            get("/{id}") {
                val intermediaryId = call.parameters["id"]!!
                val useCase by closestDI().instance<GetIntermediaryByIdService>()
                call.respond(
                    status = HttpStatusCode.OK,
                    message = useCase.get(
                        userId = parseUuid(jwtUserId()),
                        intermediaryId = parseUuid(intermediaryId)
                    ).toViewModel()
                )
            }
        }
    }
}