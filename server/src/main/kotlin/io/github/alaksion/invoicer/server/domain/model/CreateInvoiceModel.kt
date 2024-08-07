package io.github.alaksion.invoicer.server.domain.model

import kotlinx.datetime.LocalDate

data class CreateInvoiceModel(
    val externalId: String,
    val senderCompanyName: String,
    val senderCompanyAddress: String,
    val recipientCompanyName: String,
    val recipientCompanyAddress: String,
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val beneficiaryName: String,
    val beneficiaryIban: String,
    val beneficiarySwift: String,
    val beneficiaryBankName: String,
    val beneficiaryBankAddress: String,
    val intermediaryIban: String? = null,
    val intermediarySwift: String? = null,
    val intermediaryBankName: String? = null,
    val intermediaryBankAddress: String? = null,
    val activities: List<CreateInvoiceActivityModel>,
)


data class CreateInvoiceActivityModel(
    val description: String,
    val unitPrice: Long,
    val quantity: Int
)
