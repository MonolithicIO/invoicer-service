package invoicer.alaksiondev.com.routes

import invoicer.alaksiondev.com.domain.models.CreateInvoiceModel
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.invoiceRouting() {


    routing {
        route("invoice") {
            get(":id") {
                call.receiveText()
            }
            get {

            }
            post {
                val text = call.receive<CreateInvoiceModel>()
                call.respond(text)
            }
            delete {

            }
        }
    }
}