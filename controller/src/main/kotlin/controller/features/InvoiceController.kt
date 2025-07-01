package controller.features

import controller.viewmodel.invoice.InvoiceDownloadLinkViewModel
import controller.viewmodel.invoice.toViewModel
import io.github.alaksion.invoicer.foundation.authentication.token.jwt.jwtProtected
import io.github.alaksion.invoicer.foundation.authentication.token.jwt.jwtUserId
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import services.api.services.invoice.DeleteInvoiceService
import services.api.services.invoice.GetUserInvoiceByIdService
import services.api.services.pdf.GenerateInvoicePdfService
import services.api.services.pdf.InvoicePdfSecureLinkService
import utils.exceptions.http.notFoundError

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
                        )?.toViewModel() ?: notFoundError("Invoice not found")
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