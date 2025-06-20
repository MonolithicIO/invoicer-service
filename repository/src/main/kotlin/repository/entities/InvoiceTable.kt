package repository.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

internal object InvoiceTable : UUIDTable("t_invoice") {
    val invoicerNumber = varchar("invoicer_number", 36)
    val issueDate = timestamp("issue_date")
    val dueDate = timestamp("due_date")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val isDeleted = bool("is_deleted")
    val company = reference("company_id", UserCompanyTable)
    val customer = reference("customer_id", CustomerTable)

    // Payment details must be consistent. Once the document is created, the payment details should not change.
    val primarySwift = varchar("primary_swift_id", 50)
    val primaryIban = varchar("primary_iban_id", 50)
    val primaryBankName = varchar("primary_bank_name", 100)
    val primaryBankAddress = varchar("primary_bank_address", 500)

    // Payment details must be nullable to allow for invoices without intermediary banks
    val intermediarySwift = varchar("intermediary_swift_id", 50).nullable()
    val intermediaryIban = varchar("intermediary_iban_id", 50).nullable()
    val intermediaryBankName = varchar("intermediary_bank_name", 100).nullable()
    val intermediaryBankAddress = varchar("intermediary_bank_address", 500).nullable()
}

internal class InvoiceEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<InvoiceEntity>(InvoiceTable)

    var invoicerNumber by InvoiceTable.invoicerNumber
    var issueDate by InvoiceTable.issueDate
    var dueDate by InvoiceTable.dueDate
    var createdAt by InvoiceTable.createdAt
    var updatedAt by InvoiceTable.updatedAt
    var isDeleted by InvoiceTable.isDeleted
    var company by UserCompanyEntity referencedOn InvoiceTable.company
    var customer by CustomerEntity referencedOn InvoiceTable.customer

    // Payment details must be consistent. Once the document is created, the payment details should not change.
    var primarySwift by InvoiceTable.primarySwift
    var primaryIban by InvoiceTable.primaryIban
    var primaryBankName by InvoiceTable.primaryBankName
    var primaryBankAddress by InvoiceTable.primaryBankAddress

    // Payment details must be nullable to allow for invoices without intermediary banks
    var intermediarySwift by InvoiceTable.intermediarySwift
    var intermediaryIban by InvoiceTable.intermediaryIban
    var intermediaryBankName by InvoiceTable.intermediaryBankName
    var intermediaryBankAddress by InvoiceTable.intermediaryBankAddress
}