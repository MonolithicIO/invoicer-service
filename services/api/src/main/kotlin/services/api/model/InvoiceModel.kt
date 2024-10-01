package services.api.model

import kotlinx.datetime.LocalDate
import services.api.model.beneficiary.BeneficiaryModel
import services.api.model.intermediary.IntermediaryModel
import services.api.model.user.UserModel
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