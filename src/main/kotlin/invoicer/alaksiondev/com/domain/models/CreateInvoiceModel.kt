package invoicer.alaksiondev.com.domain.models

import invoicer.alaksiondev.com.serializers.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
internal data class CreateInvoiceModel(
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
    val intermediary: CreateInvoiceIntermediaryModel? = null
)

@Serializable
internal data class CreateInvoiceBeneficiaryModel(
    val beneficiaryName: String,
    val beneficiaryIban: String,
    val beneficiarySwift: String,
    val beneficiaryBankName: String,
    val beneficiaryBankAddress: String,
)

@Serializable
internal data class CreateInvoiceIntermediaryModel(
    val intermediaryIban: String,
    val intermediarySwift: String,
    val intermediaryBankName: String,
    val intermediaryBankAddress: String,
)