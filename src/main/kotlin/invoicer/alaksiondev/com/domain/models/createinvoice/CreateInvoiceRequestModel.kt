package invoicer.alaksiondev.com.domain.models.createinvoice

import invoicer.alaksiondev.com.serializers.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CreateInvoiceModel(
    val externalId: String,
    val senderCompanyName: String,
    val senderCompanyAddress: String,
    val recipientCompanyName: String,
    val recipientCompanyAddress: String,
    @Serializable(with = LocalDateSerializer::class)
    val issueDate: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val dueDate: LocalDate,
    val beneficiary: CreateInvoiceBeneficiaryModel,
    val intermediary: CreateInvoiceIntermediaryModel? = null,
    val services: List<CreateInvoiceServiceModel>
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
data class CreateInvoiceServiceModel(
    val description: String,
    val unitPrice: Long,
    val quantity: Long
)