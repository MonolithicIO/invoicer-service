package controller.features

import controller.viewmodel.beneficiary.CreateBeneficiaryResponseViewModel
import controller.viewmodel.company.CreateCompanyViewModel
import controller.viewmodel.company.toModel
import controller.viewmodel.company.toViewModel
import controller.viewmodel.customer.CreateCustomerResponseViewModel
import controller.viewmodel.customer.CreateCustomerViewModel
import controller.viewmodel.customer.toModel
import foundation.authentication.impl.jwt.jwtProtected
import foundation.authentication.impl.jwt.jwtUserId
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.company.CreateCompanyService
import services.api.services.company.GetCompaniesService
import services.api.services.customer.CreateCustomerService

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
            get {
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

        jwtProtected {
            post("/{companyId}/customer") {
                val companyId = call.parameters["companyId"]!!
                val body = call.receive<CreateCustomerViewModel>()
                val service by closestDI().instance<CreateCustomerService>()

                call.respond(
                    status = HttpStatusCode.Created,
                    message = CreateCustomerResponseViewModel(
                        service.createCustomer(
                            userId = parseUuid(jwtUserId()),
                            data = body.toModel(companyId)
                        )
                    )
                )
            }
        }
    }
}