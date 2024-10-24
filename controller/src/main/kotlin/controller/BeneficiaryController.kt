package controller

import controller.viewmodel.beneficiary.CreateBeneficiaryResponseViewModel
import controller.viewmodel.beneficiary.CreateBeneficiaryViewModel
import controller.viewmodel.beneficiary.UpdateBeneficiaryViewModel
import controller.viewmodel.beneficiary.UserBeneficiariesViewModel
import controller.viewmodel.beneficiary.toModel
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.beneficiary.CreateBeneficiaryService
import services.api.services.beneficiary.DeleteBeneficiaryService
import services.api.services.beneficiary.GetUserBeneficiariesService
import services.api.services.beneficiary.UpdateBeneficiaryService
import utils.authentication.api.jwt.jwtProtected
import utils.authentication.api.jwt.jwtUserId

internal fun Routing.beneficiaryController() {
    route("/beneficiary") {
        jwtProtected {
            post {
                val body = call.receive<CreateBeneficiaryViewModel>()
                val model = body.toModel()
                val useCase by closestDI().instance<CreateBeneficiaryService>()

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
                val useCase by closestDI().instance<DeleteBeneficiaryService>()

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
                val useCase by closestDI().instance<GetUserBeneficiariesService>()

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

        jwtProtected {
            put("/{id}") {
                val beneficiaryId = call.parameters["id"]!!
                val useCase by closestDI().instance<UpdateBeneficiaryService>()
                val userId = jwtUserId()
                val body = call.receive<UpdateBeneficiaryViewModel>()
                call.respond(
                    status = HttpStatusCode.OK,
                    message = useCase.execute(
                        beneficiaryId = beneficiaryId,
                        model = body.toModel(),
                        userId = userId
                    ).toModel()
                )
            }
        }
    }
}