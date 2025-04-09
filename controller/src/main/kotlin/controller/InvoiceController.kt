package controller

import controller.viewmodel.invoice.*
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
import services.api.services.invoice.CreateInvoiceService
import services.api.services.invoice.DeleteInvoiceService
import services.api.services.invoice.GetUserInvoiceByIdService
import services.api.services.invoice.GetUserInvoicesService
import services.api.services.pdf.GenerateInvoicePdfService
import services.api.services.pdf.InvoicePdfSecureLinkService

internal fun Routing.invoiceController() {
    route("/v1/invoice") {

        jwtProtected {
            get("/{id}") {
                val invoiceId = call.parameters["id"]
                val findOneService by closestDI().instance<GetUserInvoiceByIdService>()

                call.respond(
                    status = HttpStatusCode.OK,
                    message =
                        findOneService.get(
                            invoiceId = parseUuid(invoiceId!!),
                            userId = parseUuid(jwtUserId())
                        ).toViewModel()
                )
            }

        }

        jwtProtected {
            get {
                val page = call.request.queryParameters["page"]?.toLongOrNull() ?: 0
                val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
                val filters = GetInvoicesFilterViewModel(
                    minIssueDate = call.request.queryParameters["minIssueDate"],
                    maxIssueDate = call.request.queryParameters["maxIssueDate"],
                    minDueDate = call.request.queryParameters["minDueDate"],
                    maxDueDate = call.request.queryParameters["maxDueDate"],
                    senderCompanyName = call.request.queryParameters["senderCompanyName"],
                    recipientCompanyName = call.request.queryParameters["recipientCompanyName"],
                )
                val findService by closestDI().instance<GetUserInvoicesService>()

                call.respond(
                    message = findService.get(
                        filters = receiveGetInvoicesFilterViewModel(filters),
                        limit = limit,
                        page = page,
                        userId = parseUuid(jwtUserId())
                    ).toViewModel(),
                    status = HttpStatusCode.OK
                )
            }
        }

        jwtProtected {
            post {
                val body = call.receive<CreateInvoiceViewModel>()
                val createService by closestDI().instance<CreateInvoiceService>()
                val response = createService.createInvoice(
                    model = body.toModel(),
                    userId = parseUuid(jwtUserId())
                )
                call.respond(
                    message = CreateInvoiceResponseViewModel(
                        externalInvoiceId = response.externalInvoiceId,
                        invoiceId = response.invoiceId
                    ),
                    status = HttpStatusCode.Created
                )
            }
        }

        jwtProtected {
            delete("/{id}") {
                val invoiceId = call.parameters["id"]!!
                val deleteUseCase by closestDI().instance<DeleteInvoiceService>()
                deleteUseCase.delete(invoiceId = parseUuid(invoiceId), userId = parseUuid(jwtUserId()))
                call.respond(HttpStatusCode.NoContent)
            }
        }

        jwtProtected {
            post("/{id}/pdf") {
                val invoiceId = call.parameters["id"]!!
                val generateService by closestDI().instance<GenerateInvoicePdfService>()

                generateService.generate(
                    invoiceId = parseUuid(invoiceId),
                    userId = parseUuid(jwtUserId())
                )
                call.respond(HttpStatusCode.OK)
            }
        }

        jwtProtected {
            get("/{id}/download_link") {
                val invoiceId = call.parameters["id"]!!
                val generateService by closestDI().instance<InvoicePdfSecureLinkService>()

                call.respond(
                    message = InvoiceDownloadLinkViewModel(
                        generateService.generate(
                            invoiceId = parseUuid(invoiceId),
                            userId = parseUuid(jwtUserId())
                        )
                    ),
                    status = HttpStatusCode.OK
                )
            }
        }
    }
}