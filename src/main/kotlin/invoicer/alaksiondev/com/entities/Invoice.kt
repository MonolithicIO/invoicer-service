package invoicer.alaksiondev.com.entities

import invoicer.alaksiondev.com.models.InvoiceModel
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import java.time.LocalDate
import java.util.UUID


object InvoiceTable : Table<InvoiceEntity>("t_invoice") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val externalId = varchar("external_id").bindTo { it.externalId }
    val senderCompanyName = varchar("sender_company_name").bindTo { it.senderCompanyName }
    val senderCompanyAddress = varchar("sender_company_address").bindTo { it.senderCompanyAddress }
    val recipientCompanyName = varchar("recipient_company_name").bindTo { it.recipientCompanyName }
    val recipientCompanyAddress =
        varchar("recipient_company_address").bindTo { it.recipientCompanyAddress }
    val issueDate = date("issue_date").bindTo { it.issueDate }
    val dueDate = date("due_date").bindTo { it.dueDate }
    val beneficiaryName = varchar("beneficiary_name").bindTo { it.beneficiaryName }
    val beneficiaryIban = varchar("beneficiary_iban").bindTo { it.beneficiaryIban }
    val beneficiarySwift = varchar("beneficiary_swift").bindTo { it.beneficiarySwift }
    val beneficiaryBankName = varchar("beneficiary_bank_name").bindTo { it.beneficiaryBankName }
    val beneficiaryBankAddress =
        varchar("beneficiary_bank_address").bindTo { it.beneficiaryBankAddress }
    val intermediaryIban = varchar("intermediary_iban").bindTo { it.intermediaryIban }
    val intermediarySwift = varchar("intermediary_swift").bindTo { it.intermediarySwift }
    val intermediaryBankName = varchar("intermediary_bank_name").bindTo { it.intermediaryBankName }
    val intermediaryBankAddress =
        varchar("intermediary_bank_address").bindTo { it.intermediaryBankAddress }
}

interface InvoiceEntity : Entity<InvoiceEntity> {
    companion object : Entity.Factory<InvoiceEntity>()

    val id: UUID
    var externalId: String
    var senderCompanyName: String
    var senderCompanyAddress: String
    var recipientCompanyAddress: String
    var recipientCompanyName: String
    var issueDate: LocalDate
    var dueDate: LocalDate
    var beneficiaryName: String
    var beneficiaryIban: String
    var beneficiarySwift: String
    var beneficiaryBankName: String
    var beneficiaryBankAddress: String
    var intermediaryIban: String?
    var intermediarySwift: String?
    var intermediaryBankName: String?
    var intermediaryBankAddress: String?
}

internal fun InvoiceEntity.toInvoiceModel(): InvoiceModel {
    return InvoiceModel(
        id = this.id.toString(),
        externalId = this.externalId,
        senderCompanyName = this.senderCompanyName,
        senderCompanyAddress = this.senderCompanyAddress,
        recipientCompanyAddress = this.recipientCompanyAddress,
        recipientCompanyName = this.recipientCompanyName,
        issueDate = this.issueDate.toString(),
        dueDate = this.dueDate.toString(),
        beneficiaryName = this.beneficiaryName,
        beneficiaryIban = this.beneficiaryIban,
        beneficiarySwift = this.beneficiarySwift,
        beneficiaryBankName = this.beneficiaryBankName,
        beneficiaryBankAddress = this.beneficiaryBankAddress,
        intermediaryIban = this.intermediaryIban,
        intermediarySwift = this.intermediarySwift,
        intermediaryBankName = this.intermediaryBankName,
        intermediaryBankAddress = this.intermediaryBankAddress,
    )
}
