package io.github.alaksion.invoicer.server.domain.model

import io.github.alaksion.invoicer.server.domain.model.user.UserModel
import kotlinx.datetime.LocalDate
import java.util.*

data class InvoiceModel(
    val id: UUID,
    val externalId: String,
    val senderCompanyName: String,
    val senderCompanyAddress: String,
    val recipientCompanyAddress: String,
    val recipientCompanyName: String,
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val beneficiaryName: String,
    val beneficiaryIban: String,
    val beneficiarySwift: String,
    val beneficiaryBankName: String,
    val beneficiaryBankAddress: String,
    val intermediaryIban: String?,
    val intermediarySwift: String?,
    val intermediaryBankName: String?,
    val intermediaryBankAddress: String?,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
    val activities: List<InvoiceModelActivityModel>,
    val user: UserModel
)

data class InvoiceModelActivityModel(
    val id: UUID,
    val name: String,
    val unitPrice: Long,
    val quantity: Int
) {
    val amount = unitPrice * quantity
}