package io.github.alaksion.invoicer.server.view.controller

import io.github.alaksion.invoicer.server.domain.usecase.invoice.CreateInvoiceUseCase
import io.github.alaksion.invoicer.server.domain.usecase.invoice.DeleteInvoiceUseCase
import io.github.alaksion.invoicer.server.domain.usecase.invoice.GetInvoiceByIdUseCase
import io.github.alaksion.invoicer.server.domain.usecase.invoice.GetInvoicesUseCase
import io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.request.CreateInvoiceViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.request.receiveCreateInvoiceViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.request.GetInvoicesFilterViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.request.receiveGetInvoicesFilterViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.response.toViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.invoicedetails.response.InvoiceDetailsViewModelSender
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import utils.authentication.api.jwt.jwtProtected
import utils.authentication.api.jwt.jwtUserId

fun Routing.invoiceController() {
    route("invoice") {

        jwtProtected {
            get("/{id}") {
                val invoiceId = call.parameters["id"]
                val findOneService by closestDI().instance<GetInvoiceByIdUseCase>()
                val sender by closestDI().instance<InvoiceDetailsViewModelSender>()

                call.respond(
                    status = HttpStatusCode.OK,
                    message = sender.send(
                        findOneService.get(
                            id = invoiceId!!,
                            userId = jwtUserId()
                        )
                    )
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
                val findService by closestDI().instance<GetInvoicesUseCase>()

                call.respond(
                    message = findService.get(
                        filters = receiveGetInvoicesFilterViewModel(filters),
                        limit = limit,
                        page = page,
                        userId = jwtUserId()
                    ).toViewModel(),
                    status = HttpStatusCode.OK
                )
            }
        }

        jwtProtected {
            post {
                val body = call.receive<CreateInvoiceViewModel>()
                val createService by closestDI().instance<CreateInvoiceUseCase>()
                val response = createService.createInvoice(
                    model = receiveCreateInvoiceViewModel(body),
                    userId = jwtUserId()
                )
                call.respond(
                    message = response,
                    status = HttpStatusCode.Created
                )
            }
        }


        jwtProtected {
            delete("/{id}") {
                val invoiceId = call.parameters["id"]!!
                val deleteUseCase by closestDI().instance<DeleteInvoiceUseCase>()
                deleteUseCase.delete(invoiceId = invoiceId, userId = jwtUserId())
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}