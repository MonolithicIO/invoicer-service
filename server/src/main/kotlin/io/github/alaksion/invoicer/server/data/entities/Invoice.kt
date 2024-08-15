package io.github.alaksion.invoicer.server.data.entities

import io.github.alaksion.invoicer.server.domain.model.InvoiceModel
import io.github.alaksion.invoicer.server.domain.model.InvoiceModelActivityModel
import io.github.alaksion.invoicer.server.domain.model.getinvoices.InvoiceListItemModel
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.date
import java.util.UUID


internal object InvoiceTable : UUIDTable("t_invoice") {
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

internal class InvoiceEntity(id: EntityID<UUID>) : UUIDEntity(id) {
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

internal fun InvoiceEntity.toModel(): InvoiceModel {
    return InvoiceModel(
        id = this.id.value,
        externalId = this.externalId,
        senderCompanyName = this.senderCompanyName,
        senderCompanyAddress = this.senderCompanyAddress,
        recipientCompanyAddress = this.recipientCompanyAddress,
        recipientCompanyName = this.recipientCompanyName,
        issueDate = this.issueDate,
        dueDate = this.dueDate,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        activities = this.activities.map {
            InvoiceModelActivityModel(
                name = it.description,
                quantity = it.quantity,
                unitPrice = it.unitPrice,
                id = it.id.value
            )
        },
        user = this.user.toModel(),
        intermediary = this.intermediary?.toModel(),
        beneficiary = this.beneficiary.toModel()
    )
}

internal fun InvoiceEntity.toListItemModel(): InvoiceListItemModel {
    return InvoiceListItemModel(
        id = this.id.value,
        externalId = this.externalId,
        senderCompany = this.senderCompanyName,
        recipientCompany = this.recipientCompanyName,
        issueDate = this.issueDate,
        dueDate = this.dueDate,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        totalAmount = this.activities
            .map { it.quantity * it.unitPrice }
            .reduce { acc, l -> acc + l },
    )
}