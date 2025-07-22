package io.github.monolithic.invoicer.controller.features

import io.github.monolithic.invoicer.controller.Constants
import io.github.monolithic.invoicer.controller.viewmodel.invoice.CreateInvoiceResponseViewModel
import io.github.monolithic.invoicer.controller.viewmodel.invoice.CreateInvoiceViewModel
import io.github.monolithic.invoicer.controller.viewmodel.invoice.InvoiceSecureLinkViewModel
import io.github.monolithic.invoicer.controller.viewmodel.invoice.getInvoiceFilters
import io.github.monolithic.invoicer.controller.viewmodel.invoice.toModel
import io.github.monolithic.invoicer.controller.viewmodel.invoice.toViewModel
import io.github.monolithic.invoicer.foundation.authentication.token.jwt.jwtProtected
import io.github.monolithic.invoicer.foundation.authentication.token.jwt.jwtUserId
import io.github.monolithic.invoicer.services.invoice.CreateInvoiceService
import io.github.monolithic.invoicer.services.invoice.GetCompanyInvoicesService
import io.github.monolithic.invoicer.services.invoice.GetUserInvoiceByIdService
import io.github.monolithic.invoicer.services.pdf.InvoicePdfSecureLinkService
import io.github.monolithic.invoicer.utils.uuid.parseUuid
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

internal fun Routing.invoiceController() {
    route("/v1/company/{companyId}") {
        createInvoice()
        listInvoices()
        invoiceDetails()
        pdfSecureLink()
    }
}

private fun Route.createInvoice() = jwtProtected {
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

private fun Route.listInvoices() = jwtProtected {
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

private fun Route.invoiceDetails() = jwtProtected {
    get("/invoice/{id}") {
        val invoiceId = call.parameters["id"]
        val companyId = call.parameters["companyId"]!!
        val service by closestDI().instance<GetUserInvoiceByIdService>()

        call.respond(
            status = HttpStatusCode.OK,
            message = service.get(
                invoiceId = parseUuid(invoiceId!!),
                userId = parseUuid(jwtUserId()),
                companyId = parseUuid(companyId)
            ).toViewModel()
        )
    }

}

private fun Route.pdfSecureLink() = jwtProtected {
    get("/invoice/{id}/pdf/secure-link") {
        val invoiceId = call.parameters["id"]!!
        val companyId = call.parameters["companyId"]!!
        val service by closestDI().instance<InvoicePdfSecureLinkService>()

        call.respond(
            status = HttpStatusCode.OK,
            message = InvoiceSecureLinkViewModel(
                service.generate(
                    invoiceId = parseUuid(invoiceId),
                    companyId = parseUuid(companyId),
                    userId = parseUuid(jwtUserId())
                )
            )
        )
    }
}
