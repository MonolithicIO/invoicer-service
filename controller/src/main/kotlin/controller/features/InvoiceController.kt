package controller.features

import controller.Constants
import controller.viewmodel.invoice.CreateInvoiceResponseViewModel
import controller.viewmodel.invoice.CreateInvoiceViewModel
import controller.viewmodel.invoice.getInvoiceFilters
import controller.viewmodel.invoice.toModel
import controller.viewmodel.invoice.toViewModel
import io.github.alaksion.invoicer.foundation.authentication.token.jwt.jwtProtected
import io.github.alaksion.invoicer.foundation.authentication.token.jwt.jwtUserId
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlin.getValue
import kotlin.text.toIntOrNull
import kotlin.text.toLongOrNull
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.invoice.CreateInvoiceService
import services.api.services.invoice.GetCompanyInvoicesService
import services.api.services.invoice.GetUserInvoiceByIdService
import utils.exceptions.http.notFoundError

internal fun Routing.invoiceController() {
    route("/v1/company/{companyId}") {

        jwtProtected {
            post("/invoice") {
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
            get("/invoices") {
                val companyId = call.parameters["companyId"]!!
                val page = call.request.queryParameters["page"]?.toLongOrNull() ?: Constants.DEFAULT_PAGE
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: Constants.DEFAULT_PAGE_LIMIT
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

        jwtProtected {
            get("/invoice/{id}") {
                val invoiceId = call.parameters["id"]
                val findOneService by closestDI().instance<GetUserInvoiceByIdService>()

                call.respond(
                    status = HttpStatusCode.OK,
                    message = findOneService.get(
                        invoiceId = parseUuid(invoiceId!!),
                    )?.toViewModel() ?: notFoundError("Invoice not found"),
                )
            }

        }
    }
}
