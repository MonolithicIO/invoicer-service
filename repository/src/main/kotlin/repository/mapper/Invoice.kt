package repository.mapper

import models.InvoiceModelActivityModelLegacy
import models.InvoiceModelLegacy
import models.getinvoices.InvoiceListItemModelLegacy
import models.invoice.*
import repository.entities.InvoiceEntity
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

internal fun InvoiceEntityLegacy.toListItemModel(): InvoiceListItemModelLegacy {
    return InvoiceListItemModelLegacy(
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

internal fun InvoiceEntity.toModel(): InvoiceModel {
    return InvoiceModel(
        id = id.value,
        invoicerNumber = invoicerNumber,
        issueDate = issueDate,
        dueDate = dueDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isDeleted = isDeleted,
        company = InvoiceCompanyModel(
            name = companyName,
            document = companyDocument,
            addressLine1 = companyAddressLine1,
            addressLine2 = companyAddressLine2,
            city = companyCity,
            zipCode = companyZipCode,
            state = companyState,
            countryCode = companyCountryCode
        ),
        primaryAccount = InvoicePayAccountModel(
            swift = primarySwift,
            iban = primaryIban,
            bankName = primaryBankName,
            bankAddress = primaryBankAddress
        ),
        intermediaryAccount = getIntermediaryAccount(),
        customer = InvoiceCustomerModel(
            name = customerName
        ),
    )
}

internal fun InvoiceEntity.toListItemModel(): InvoiceListItemModel {
    return InvoiceListItemModel(
        id = id.value,
        invoiceNumber = invoicerNumber,
        // TODO - Calculate invoice items
        amount = 0
    )
}

private fun InvoiceEntity.getIntermediaryAccount(): InvoicePayAccountModel? {
    return InvoicePayAccountModel(
        swift = intermediarySwift ?: return null,
        iban = intermediaryIban ?: return null,
        bankName = intermediaryBankName ?: return null,
        bankAddress = intermediaryBankAddress ?: return null
    )
}