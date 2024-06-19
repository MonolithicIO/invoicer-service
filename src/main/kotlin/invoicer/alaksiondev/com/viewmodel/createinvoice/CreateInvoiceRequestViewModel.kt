package invoicer.alaksiondev.com.viewmodel.createinvoice

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CreateInvoiceModel(
    val externalId: String,
    val senderCompanyName: String,
    val senderCompanyAddress: String,
    val recipientCompanyName: String,
    val recipientCompanyAddress: String,
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val beneficiary: CreateInvoiceBeneficiaryModel,
    val intermediary: CreateInvoiceIntermediaryModel? = null,
    val activities: List<CreateInvoiceActivityModel>
)

@Serializable
data class CreateInvoiceBeneficiaryModel(
    val beneficiaryName: String,
    val beneficiaryIban: String,
    val beneficiarySwift: String,
    val beneficiaryBankName: String,
    val beneficiaryBankAddress: String,
)

@Serializable
data class CreateInvoiceIntermediaryModel(
    val intermediaryIban: String,
    val intermediarySwift: String,
    val intermediaryBankName: String,
    val intermediaryBankAddress: String,
)

@Serializable
data class CreateInvoiceActivityModel(
    val description: String,
    val unitPrice: Long,
    val quantity: Int
)