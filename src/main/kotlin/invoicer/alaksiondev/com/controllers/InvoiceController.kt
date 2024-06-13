package invoicer.alaksiondev.com.controllers

import invoicer.alaksiondev.com.models.createinvoice.CreateInvoiceModel
import invoicer.alaksiondev.com.services.CreateInvoicePdfService
import invoicer.alaksiondev.com.services.CreateInvoiceService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.request.receiveParameters
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.invoiceController() {


    routing {
        route("invoice") {
            get(":id") {
                call.receiveText()
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