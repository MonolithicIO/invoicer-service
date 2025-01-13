package models

import kotlinx.datetime.Instant
import models.beneficiary.BeneficiaryModel
import models.intermediary.IntermediaryModel
import models.user.UserModel
import java.util.*

data class InvoiceModel(
    val id: UUID,
    val externalId: String,
    val senderCompanyName: String,
    val senderCompanyAddress: String,
    val recipientCompanyAddress: String,
    val recipientCompanyName: String,
    val issueDate: Instant,
    val dueDate: Instant,
    val createdAt: Instant,
    val updatedAt: Instant,
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
)