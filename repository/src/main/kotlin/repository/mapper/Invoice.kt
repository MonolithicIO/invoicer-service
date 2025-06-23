package repository.mapper

import models.invoice.*
import repository.entities.InvoiceEntity

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