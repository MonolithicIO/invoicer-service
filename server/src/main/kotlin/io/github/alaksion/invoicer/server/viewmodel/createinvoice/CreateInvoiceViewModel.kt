package io.github.alaksion.invoicer.server.viewmodel.createinvoice

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CreateInvoiceViewModel(
    val externalId: String,
    val senderCompanyName: String,
    val senderCompanyAddress: String,
    val recipientCompanyName: String,
    val recipientCompanyAddress: String,
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val beneficiary: CreateInvoiceBeneficiaryViewModel,
    val intermediary: CreateInvoiceIntermediaryViewModel? = null,
    val activities: List<CreateInvoiceActivityViewModel>
)

@Serializable
data class CreateInvoiceBeneficiaryViewModel(
    val beneficiaryName: String,
    val beneficiaryIban: String,
    val beneficiarySwift: String,
    val beneficiaryBankName: String,
    val beneficiaryBankAddress: String,
)

@Serializable
data class CreateInvoiceIntermediaryViewModel(
    val intermediaryIban: String,
    val intermediarySwift: String,
    val intermediaryBankName: String,
    val intermediaryBankAddress: String,
)

@Serializable
data class CreateInvoiceActivityViewModel(
    val description: String,
    val unitPrice: Long,
    val quantity: Int
)