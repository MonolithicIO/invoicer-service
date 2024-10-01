package repository.api.mapper

import entities.InvoiceEntity
import services.api.model.InvoiceModel
import services.api.model.InvoiceModelActivityModel
import services.api.model.getinvoices.InvoiceListItemModel

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