package io.github.monolithic.invoicer.repository.mapper

import io.github.monolithic.invoicer.repository.entities.InvoiceEntity
import io.github.monolithic.invoicer.models.invoice.InvoiceCompanyModel
import io.github.monolithic.invoicer.models.invoice.InvoiceCustomerModel
import io.github.monolithic.invoicer.models.invoice.InvoiceListItemModel
import io.github.monolithic.invoicer.models.invoice.InvoiceModel
import io.github.monolithic.invoicer.models.invoice.InvoiceModelActivityModel
import io.github.monolithic.invoicer.models.invoice.InvoicePayAccountModel

internal fun InvoiceEntity.toModel(): InvoiceModel {
    return InvoiceModel(
        id = id.value,
        invoiceNumber = invoicerNumber,
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
            countryCode = companyCountryCode,
            id = companyId.value,
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
        activities = activities.map {
            InvoiceModelActivityModel(
                id = it.id.value,
                quantity = it.quantity,
                unitPrice = it.unitPrice,
                name = it.description,
            )
        }
    )
}

internal fun InvoiceEntity.toListItemModel(): InvoiceListItemModel {
    return InvoiceListItemModel(
        id = id.value,
        invoiceNumber = invoicerNumber,
        companyName = companyName,
        customerName = customerName,
        issueDate = issueDate,
        dueDate = dueDate,
        createdAt = createdAt,
        updatedAt = updatedAt,
        totalAmount = activities.sumOf { it.quantity * it.unitPrice }
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
