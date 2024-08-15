package io.github.alaksion.invoicer.server.domain.model

import io.github.alaksion.invoicer.server.domain.model.beneficiary.BeneficiaryModel
import io.github.alaksion.invoicer.server.domain.model.intermediary.IntermediaryModel
import io.github.alaksion.invoicer.server.domain.model.user.UserModel
import kotlinx.datetime.LocalDate
import java.util.UUID

data class InvoiceModel(
    val id: UUID,
    val externalId: String,
    val senderCompanyName: String,
    val senderCompanyAddress: String,
    val recipientCompanyAddress: String,
    val recipientCompanyName: String,
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
    val activities: List<InvoiceModelActivityModel>,
    val user: UserModel,
    val beneficiary: BeneficiaryModel,
    val intermediary: IntermediaryModel?
)

data class InvoiceModelActivityModel(
    val id: UUID,
    val name: String,
    val unitPrice: Long,
    val quantity: Int
) {
    val amount = unitPrice * quantity
}