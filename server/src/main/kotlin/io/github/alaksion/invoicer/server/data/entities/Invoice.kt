package io.github.alaksion.invoicer.server.data.entities

import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.kotlin.datetime.date
import java.util.*


internal object InvoiceTable : UUIDTable("t_invoice") {
    val externalId = varchar("external_id", 36)
    val senderCompanyName = varchar("sender_company_name", 500)
    val senderCompanyAddress = varchar("sender_company_address", 1000)
    val recipientCompanyName = varchar("recipient_company_name", 500)
    val recipientCompanyAddress = varchar("recipient_company_address", 1000)
    val issueDate: Column<LocalDate> = date("issue_date")
    val dueDate = date("due_date")
    val beneficiaryName = varchar("beneficiary_name", 500)
    val beneficiaryIban = varchar("beneficiary_iban", 100)
    val beneficiarySwift = varchar("beneficiary_swift", 11)
    val beneficiaryBankName = varchar("beneficiary_bank_name", 500)
    val beneficiaryBankAddress = varchar("beneficiary_bank_address", 1000)
    val intermediaryIban = varchar("intermediary_iban", 100).nullable()
    val intermediarySwift = varchar("intermediary_swift", 11).nullable()
    val intermediaryBankName = varchar("intermediary_bank_name", 500).nullable()
    val intermediaryBankAddress = varchar("intermediary_bank_address", 1000).nullable()
    val createdAt = date("created_at")
    val updatedAt = date("updated_at")
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
    var beneficiaryName by InvoiceTable.beneficiaryName
    var beneficiaryIban by InvoiceTable.beneficiaryIban
    var beneficiarySwift by InvoiceTable.beneficiarySwift
    var beneficiaryBankName by InvoiceTable.beneficiaryBankName
    var beneficiaryBankAddress by InvoiceTable.beneficiaryBankAddress
    var intermediaryIban by InvoiceTable.intermediaryIban
    var intermediarySwift by InvoiceTable.intermediarySwift
    var intermediaryBankName by InvoiceTable.intermediaryBankName
    var intermediaryBankAddress by InvoiceTable.intermediaryBankAddress
    var createdAt by InvoiceTable.createdAt
    var updatedAt by InvoiceTable.updatedAt
    val activities by InvoiceActivityEntity referrersOn InvoiceActivityTable.invoice
}