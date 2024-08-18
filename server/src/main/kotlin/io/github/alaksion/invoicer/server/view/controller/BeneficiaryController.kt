package io.github.alaksion.invoicer.server.view.controller

import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.CreateBeneficiaryUseCase
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.DeleteBeneficiaryUseCase
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.GetUserBeneficiariesUseCase
import io.github.alaksion.invoicer.server.view.viewmodel.beneficiary.CreateBeneficiaryResponseViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.beneficiary.CreateBeneficiaryViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.beneficiary.UserBeneficiariesViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.beneficiary.toModel
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import utils.authentication.api.jwt.jwtProtected
import utils.authentication.api.jwt.jwtUserId

fun Routing.beneficiaryController() {
    route("/beneficiary") {
        jwtProtected {
            post {
                val body = call.receive<CreateBeneficiaryViewModel>()
                val model = body.toModel()
                val useCase by closestDI().instance<CreateBeneficiaryUseCase>()

                call.respond(
                    message =
                    CreateBeneficiaryResponseViewModel(
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
                val beneficiaryId = call.parameters["id"]!!
                val useCase by closestDI().instance<DeleteBeneficiaryUseCase>()

                useCase.execute(
                    beneficiaryId = beneficiaryId,
                    userId = jwtUserId()
                )

                call.respond(HttpStatusCode.NoContent)
            }
        }

        jwtProtected {
            get {
                val page = call.request.queryParameters["page"]?.toLongOrNull() ?: 0
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val useCase by closestDI().instance<GetUserBeneficiariesUseCase>()

                call.respond(
                    status = HttpStatusCode.OK,
                    message = UserBeneficiariesViewModel(
                        beneficiaries = useCase.execute(
                            userId = jwtUserId(),
                            page = page,
                            limit = limit
                        ).map { it.toModel() }
                    )
                )
            }
        }
    }
}