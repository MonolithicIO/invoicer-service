package io.github.alaksion.invoicer.server.view.controller

import io.github.alaksion.invoicer.server.domain.usecase.CreateInvoicePdfUseCase
import io.github.alaksion.invoicer.server.domain.usecase.CreateInvoiceUseCase
import io.github.alaksion.invoicer.server.domain.usecase.GetInvoiceByIdUseCase
import io.github.alaksion.invoicer.server.domain.usecase.GetInvoicesUseCase
import io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.request.CreateInvoiceRequestViewModelReceiver
import io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.request.CreateInvoiceViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.request.GetInvoicesFilterViewModel
import io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.request.GetInvoicesFilterViewModelReceiver
import io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.response.GetInvoicesViewModelSender
import io.github.alaksion.invoicer.server.view.viewmodel.invoicedetails.response.InvoiceDetailsViewModelSender
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.invoiceController() {


    routing {
        route("invoice") {
            get("/{id}") {
                val invoiceId = call.parameters["id"]
                val findOneService by closestDI().instance<GetInvoiceByIdUseCase>()
                val sender by closestDI().instance<InvoiceDetailsViewModelSender>()

                call.respond(
                    status = HttpStatusCode.OK,
                    message = sender.send(findOneService.get(invoiceId!!))
                )
            }
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
                val receiver by closestDI().instance<GetInvoicesFilterViewModelReceiver>()
                val findService by closestDI().instance<GetInvoicesUseCase>()
                val sender by closestDI().instance<GetInvoicesViewModelSender>()

                call.respond(
                    message = sender.send(
                        findService.get(
                            filters = receiver.receive(filters),
                            limit = limit,
                            page = page
                        )
                    ),
                    status = HttpStatusCode.OK
                )
            }
            post {
                val body = call.receive<CreateInvoiceViewModel>()

                val receiver by closestDI().instance<CreateInvoiceRequestViewModelReceiver>()
                val createService by closestDI().instance<CreateInvoiceUseCase>()

                val response = createService.createInvoice(receiver.receive(body))
                call.respond(
                    message = response,
                    status = HttpStatusCode.Created
                )
            }
            post("/pdf/{id}") {
                val invoiceId = call.parameters["id"]
                val pdfService by closestDI().instance<CreateInvoicePdfUseCase>()
                pdfService.create(invoiceId!!)
                call.respond("hehehehe")
            }
            delete {

            }
        }
    }
}