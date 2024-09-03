package entities

import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.date
import java.util.UUID


object InvoiceTable : UUIDTable("t_invoice") {
    val externalId = varchar("external_id", 36)
    val senderCompanyName = varchar("sender_company_name", 500)
    val senderCompanyAddress = varchar("sender_company_address", 1000)
    val recipientCompanyName = varchar("recipient_company_name", 500)
    val recipientCompanyAddress = varchar("recipient_company_address", 1000)
    val issueDate: Column<LocalDate> = date("issue_date")
    val dueDate = date("due_date")
    val createdAt = date("created_at")
    val updatedAt = date("updated_at")
    val user = reference(name = "user_id", foreign = UserTable, onDelete = ReferenceOption.CASCADE)
    val beneficiary = reference("beneficiary_id", foreign = BeneficiaryTable)
    val intermediary = optReference("intermediary_id", foreign = IntermediaryTable)
}

class InvoiceEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<InvoiceEntity>(InvoiceTable)

    var externalId by InvoiceTable.externalId
    var senderCompanyName by InvoiceTable.senderCompanyName
    var senderCompanyAddress by InvoiceTable.senderCompanyAddress
    var recipientCompanyAddress by InvoiceTable.recipientCompanyAddress
    var recipientCompanyName by InvoiceTable.recipientCompanyName
    var issueDate by InvoiceTable.issueDate
    var dueDate by InvoiceTable.dueDate
    var createdAt by InvoiceTable.createdAt
    var updatedAt by InvoiceTable.updatedAt
    val activities by InvoiceActivityEntity referrersOn InvoiceActivityTable.invoice
    var user by UserEntity referencedOn InvoiceTable.user
    var beneficiary by BeneficiaryEntity referencedOn InvoiceTable.beneficiary
    var intermediary by IntermediaryEntity optionalReferencedOn InvoiceTable.intermediary
}