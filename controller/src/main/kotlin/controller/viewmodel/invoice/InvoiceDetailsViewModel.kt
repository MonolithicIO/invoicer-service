package controller.viewmodel.invoice

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import models.invoice.InvoiceModel
import models.invoice.InvoiceModelActivityModel

@Serializable
internal data class InvoiceDetailsViewModel(
    val id: String,
    val invoiceNumber: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val activities: List<InvoiceDetailsActivityViewModel>,
    val primaryAccount: InvoiceDetailsPayAccountViewModel,
    val intermediaryAccount: InvoiceDetailsPayAccountViewModel?,
    val company: InvoiceDetailsCompanyViewModel,
    val customer: InvoiceDetailsCustomerModel,
)

@Serializable
data class InvoiceDetailsPayAccountViewModel(
    val swift: String,
    val iban: String,
    val bankName: String,
    val bankAddress: String,
)

@Serializable
data class InvoiceDetailsCompanyViewModel(
    val name: String,
    val document: String,
    val addressLine1: String,
    val addressLine2: String?,
    val city: String,
    val zipCode: String,
    val state: String,
    val countryCode: String,
)

@Serializable
data class InvoiceDetailsCustomerModel(
    val name: String,
)

@Serializable
internal data class InvoiceDetailsActivityViewModel(
    val id: String,
    val description: String,
    val unitPrice: Long,
    val quantity: Int
)

internal fun InvoiceModel.toViewModel(): InvoiceDetailsViewModel {
    return InvoiceDetailsViewModel(
        id = id.toString(),
        invoiceNumber = invoiceNumber,
        createdAt = createdAt,
        updatedAt = updatedAt,
        issueDate = issueDate,
        dueDate = dueDate,
        activities = makeActivities(activities),
        primaryAccount = InvoiceDetailsPayAccountViewModel(
            swift = primaryAccount.swift,
            iban = primaryAccount.iban,
            bankName = primaryAccount.bankName,
            bankAddress = primaryAccount.bankAddress
        ),
        intermediaryAccount = intermediaryAccount?.let {
            InvoiceDetailsPayAccountViewModel(
                swift = it.swift,
                iban = it.iban,
                bankName = it.bankName,
                bankAddress = it.bankAddress
            )
        },
        company = InvoiceDetailsCompanyViewModel(
            name = company.name,
            document = company.document,
            addressLine1 = company.addressLine1,
            addressLine2 = company.addressLine2,
            city = company.city,
            zipCode = company.zipCode,
            state = company.state,
            countryCode = company.countryCode,
        ),
        customer = InvoiceDetailsCustomerModel(
            name = customer.name
        )
    )
}

private fun makeActivities(activities: List<InvoiceModelActivityModel>): List<InvoiceDetailsActivityViewModel> {
    return activities.map {
        InvoiceDetailsActivityViewModel(
            id = it.id.toString(),
            description = it.name,
            unitPrice = it.unitPrice,
            quantity = it.quantity
        )
    }
}
