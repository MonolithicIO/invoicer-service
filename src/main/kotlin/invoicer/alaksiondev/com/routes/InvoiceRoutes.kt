package invoicer.alaksiondev.com.routes

import DatabaseFactory
import invoicer.alaksiondev.com.data.entities.InvoiceTable
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
import org.ktorm.dsl.insert

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
                val database = DatabaseFactory.database
                database.insert(InvoiceTable) { invoiceTable ->
                    set(invoiceTable.externalId, text.externalId)
                    set(invoiceTable.senderCompanyName, text.senderCompanyName)
                    set(invoiceTable.senderCompanyAddress, text.senderCompanyName)
                    set(invoiceTable.recipientCompanyName, text.recipientCompanyName)
                    set(invoiceTable.recipientCompanyAddress, text.recipientCompanyAddress)
                    set(invoiceTable.issueDate, text.issueDate)
                    set(invoiceTable.dueDate, text.dueDate)
                    set(invoiceTable.beneficiaryName, text.beneficiary.beneficiaryName)
                    set(invoiceTable.beneficiaryIban, text.beneficiary.beneficiaryIban)
                    set(invoiceTable.beneficiarySwift, text.beneficiary.beneficiarySwift)
                    set(invoiceTable.beneficiaryBankName, text.beneficiary.beneficiaryBankName)
                    set(
                        invoiceTable.beneficiaryBankAddress,
                        text.beneficiary.beneficiaryBankAddress
                    )

                    set(invoiceTable.intermediaryIban, text.intermediary?.intermediaryIban)
                    set(invoiceTable.intermediarySwift, text.intermediary?.intermediarySwift)
                    set(invoiceTable.intermediaryBankName, text.intermediary?.intermediaryBankName)
                    set(
                        invoiceTable.intermediaryBankAddress,
                        text.intermediary?.intermediaryBankAddress
                    )
                }
                call.respond(text)
            }
            delete {

            }
        }
    }
}