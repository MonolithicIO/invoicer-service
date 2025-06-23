package repository.mapper

import models.InvoiceModelLegacy
import models.InvoiceModelActivityModelLegacy
import models.getinvoices.InvoiceListItemModel
import repository.entities.legacy.InvoiceEntityLegacy

internal fun InvoiceEntityLegacy.toModel(): InvoiceModelLegacy {
    return InvoiceModelLegacy(
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
            InvoiceModelActivityModelLegacy(
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

internal fun InvoiceEntityLegacy.toListItemModel(): InvoiceListItemModel {
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