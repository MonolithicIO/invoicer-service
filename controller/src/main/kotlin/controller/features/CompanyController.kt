package controller.features

import controller.viewmodel.beneficiary.CreateBeneficiaryResponseViewModel
import controller.viewmodel.company.CreateCompanyViewModel
import controller.viewmodel.company.toModel
import controller.viewmodel.company.toViewModel
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
import services.api.services.company.CreateCompanyService
import services.api.services.company.GetCompaniesService

internal fun Routing.companyController() {
    route("/v1/company") {
        jwtProtected {
            post {
                val body = call.receive<CreateCompanyViewModel>()
                val service by closestDI().instance<CreateCompanyService>()

                call.respond(
                    message =
                        CreateBeneficiaryResponseViewModel(
                            id = service.createCompany(
                                data = body.toModel(),
                                userId = parseUuid(jwtUserId())
                            )
                        ),
                    status = HttpStatusCode.Created
                )
            }
        }

        jwtProtected {
            get("/v1/company") {
                val page = call.request.queryParameters["page"]?.toLongOrNull() ?: 0
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val service by closestDI().instance<GetCompaniesService>()

                call.respond(
                    status = HttpStatusCode.OK,
                    message = service.get(
                        userId = parseUuid(jwtUserId()),
                        page = page.toInt(),
                        limit = limit
                    ).toViewModel()
                )
            }
        }
    }
}