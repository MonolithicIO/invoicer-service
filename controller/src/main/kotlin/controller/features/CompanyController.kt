package controller.features

import controller.viewmodel.company.CreateCompanyResponseViewModel
import controller.viewmodel.company.CreateCompanyViewModel
import controller.viewmodel.company.toModel
import controller.viewmodel.company.toViewModel
import controller.viewmodel.customer.CreateCustomerResponseViewModel
import controller.viewmodel.customer.CreateCustomerViewModel
import controller.viewmodel.customer.toModel
import controller.viewmodel.customer.toViewModel
import controller.viewmodel.invoice.*
import io.github.alaksion.invoicer.foundation.authentication.token.jwt.jwtProtected
import io.github.alaksion.invoicer.foundation.authentication.token.jwt.jwtUserId
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.company.CreateCompanyService
import services.api.services.company.GetCompaniesService
import services.api.services.company.GetUserCompanyDetailsService
import services.api.services.customer.CreateCustomerService
import services.api.services.customer.ListCustomersService
import services.api.services.invoice.CreateInvoiceService
import services.api.services.invoice.GetCompanyInvoicesService

internal fun Routing.companyController() {
    route("/v1/company") {
        jwtProtected {
            post {
                val body = call.receive<CreateCompanyViewModel>()
                val service by closestDI().instance<CreateCompanyService>()

                call.respond(
                    message =
                        CreateCompanyResponseViewModel(
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
            get("/{companyId}") {
                val companyId = call.parameters["companyId"]!!
                val service by closestDI().instance<GetUserCompanyDetailsService>()

                call.respond(
                    message = service.get(
                        userId = parseUuid(jwtUserId()),
                        companyId = parseUuid(companyId)
                    ).toViewModel(),
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

        jwtProtected {
            get("/{companyId}/customers") {
                val companyId = call.parameters["companyId"]!!
                val page = call.request.queryParameters["page"]?.toLongOrNull() ?: 0
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val service by closestDI().instance<ListCustomersService>()

                call.respond(
                    status = HttpStatusCode.Created,
                    message = service.list(
                        userId = parseUuid(jwtUserId()),
                        page = page,
                        limit = limit,
                        companyId = parseUuid(companyId),
                    ).toViewModel()
                )
            }
        }

        jwtProtected {
            post("/{companyId}/invoice") {
                val companyId = call.parameters["companyId"]!!
                val body = call.receive<CreateInvoiceViewModel>()
                val service by closestDI().instance<CreateInvoiceService>()
                val result = service.createInvoice(
                    model = body.toModel(companyId),
                    userId = parseUuid(jwtUserId())
                )

                call.respond(
                    status = HttpStatusCode.Created,
                    message = CreateInvoiceResponseViewModel(
                        invoiceId = result.invoiceId.toString(),
                        externalInvoiceId = result.externalInvoiceId
                    )
                )
            }
        }

        jwtProtected {
            get("/{companyId}/invoices") {
                val companyId = call.parameters["companyId"]!!
                val page = call.request.queryParameters["page"]?.toLongOrNull() ?: 0
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val service by closestDI().instance<GetCompanyInvoicesService>()

                call.respond(
                    message = service.get(
                        filters = getInvoiceFilters(call.request.queryParameters),
                        limit = limit,
                        page = page,
                        userId = parseUuid(jwtUserId()),
                        companyId = parseUuid(companyId)
                    ).toViewModel(),
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}