package io.github.alaksion.invoicer.server.controllers

import io.github.alaksion.invoicer.server.viewmodel.createinvoice.CreateInvoiceModel
import io.github.alaksion.invoicer.server.service.CreateInvoicePdfService
import io.github.alaksion.invoicer.server.service.CreateInvoiceService
import io.github.alaksion.invoicer.server.service.GetInvoiceByIdService
import io.github.alaksion.invoicer.server.viewmodel.InvoiceDetailsViewModel
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
                val findOneService by closestDI().instance<GetInvoiceByIdService>()
                val response = findOneService.get(invoiceId!!)
                call.respond(
                    status = HttpStatusCode.OK,
                    message = InvoiceDetailsViewModel.Factory(response)
                )
            }
            get {

            }
            post {
                val body = call.receive<CreateInvoiceModel>()
                val createService by closestDI().instance<CreateInvoiceService>()
                val response = createService.createInvoice(body)
                call.respond(
                    message = response,
                    status = HttpStatusCode.Created
                )
            }

            post("/pdf/{id}") {
                val invoiceId = call.parameters["id"]
                val pdfService by closestDI().instance<CreateInvoicePdfService>()
                pdfService.create(invoiceId!!)
                call.respond("hehehehe")
            }
            delete {

            }
        }
    }
}