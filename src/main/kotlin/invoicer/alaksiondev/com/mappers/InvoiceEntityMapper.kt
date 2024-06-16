package invoicer.alaksiondev.com.mappers

import invoicer.alaksiondev.com.entities.InvoiceEntity
import invoicer.alaksiondev.com.models.InvoiceModel

internal fun InvoiceEntity.toDomain(
    mapActivities: Boolean
): InvoiceModel {
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
        activities = if (mapActivities) this.activities.toList().map { it.toDomain(mapInvoice = false) } else null
    )
}