package io.github.alaksion.invoicer.server.view.viewmodel.invoicedetails.response

import kotlinx.serialization.Serializable

@Serializable
data class InvoiceDetailsViewModel(
    val id: String,
    val externalId: String,
    val senderCompany: InvoiceDetailsCompanyViewModel,
    val recipientCompany: InvoiceDetailsCompanyViewModel,
    val issueDate: String,
    val dueDate: String,
    val beneficiary: InvoiceDetailsTransactionAccountViewModel,
    val intermediary: InvoiceDetailsTransactionAccountViewModel?,
    val createdAt: String,
    val updatedAt: String,
    val activities: List<InvoiceDetailsActivityViewModel>
)

@Serializable
data class InvoiceDetailsCompanyViewModel(
    val name: String,
    val address: String
)

@Serializable
data class InvoiceDetailsTransactionAccountViewModel(
    val name: String?,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
)

@Serializable
data class InvoiceDetailsActivityViewModel(
    val id: String,
    val description: String,
    val unitPrice: Long,
    val quantity: Int
)
