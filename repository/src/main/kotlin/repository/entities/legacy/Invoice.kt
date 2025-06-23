package repository.entities.legacy

import repository.entities.UserEntity
import repository.entities.UserTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*


internal object InvoiceTableLegacy : UUIDTable("t_invoice") {
    val externalId = varchar("external_id", 36)
    val senderCompanyName = varchar("sender_company_name", 500)
    val senderCompanyAddress = varchar("sender_company_address", 1000)
    val recipientCompanyName = varchar("recipient_company_name", 500)
    val recipientCompanyAddress = varchar("recipient_company_address", 1000)
    val issueDate = timestamp("issue_date")
    val dueDate = timestamp("due_date")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val isDeleted = bool("is_deleted").default(false)
    val user = reference(name = "user_id", foreign = UserTable, onDelete = ReferenceOption.CASCADE)
    val beneficiary = reference("beneficiary_id", foreign = BeneficiaryTable)
    val intermediary = optReference("intermediary_id", foreign = IntermediaryTable)
}

internal class InvoiceEntityLegacy(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<InvoiceEntityLegacy>(InvoiceTableLegacy)

    var externalId by InvoiceTableLegacy.externalId
    var senderCompanyName by InvoiceTableLegacy.senderCompanyName
    var senderCompanyAddress by InvoiceTableLegacy.senderCompanyAddress
    var recipientCompanyAddress by InvoiceTableLegacy.recipientCompanyAddress
    var recipientCompanyName by InvoiceTableLegacy.recipientCompanyName
    var issueDate by InvoiceTableLegacy.issueDate
    var dueDate by InvoiceTableLegacy.dueDate
    var createdAt by InvoiceTableLegacy.createdAt
    var updatedAt by InvoiceTableLegacy.updatedAt
    var isDeleted by InvoiceTableLegacy.isDeleted
    val activities by InvoiceActivityEntity.Companion referrersOn InvoiceActivityTable.invoice
    var user by UserEntity.Companion referencedOn InvoiceTableLegacy.user
    var beneficiary by BeneficiaryEntity.Companion referencedOn InvoiceTableLegacy.beneficiary
    var intermediary by IntermediaryEntity.Companion optionalReferencedOn InvoiceTableLegacy.intermediary
}