package io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.request

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CreateInvoiceViewModel(
    val externalId: String? = null,
    val senderCompanyName: String? = null,
    val senderCompanyAddress: String? = null,
    val recipientCompanyName: String? = null,
    val recipientCompanyAddress: String? = null,
    val issueDate: LocalDate? = null,
    val dueDate: LocalDate? = null,
    val beneficiary: CreateInvoiceBeneficiaryViewModel? = null,
    val intermediary: CreateInvoiceIntermediaryViewModel? = null,
    val activities: List<CreateInvoiceActivityViewModel> = listOf()
)

@Serializable
data class CreateInvoiceBeneficiaryViewModel(
    val beneficiaryName: String? = null,
    val beneficiaryIban: String? = null,
    val beneficiarySwift: String? = null,
    val beneficiaryBankName: String? = null,
    val beneficiaryBankAddress: String? = null,
)

@Serializable
data class CreateInvoiceIntermediaryViewModel(
    val intermediaryIban: String? = null,
    val intermediarySwift: String? = null,
    val intermediaryBankName: String? = null,
    val intermediaryBankAddress: String? = null,
)

@Serializable
data class CreateInvoiceActivityViewModel(
    val description: String? = null,
    val unitPrice: Long? = null,
    val quantity: Int? = null
)