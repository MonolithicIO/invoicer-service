package models

import io.github.alaksion.invoicer.utils.serialization.JavaUUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import models.beneficiary.BeneficiaryModel
import models.intermediary.IntermediaryModel
import models.user.UserModel
import java.util.*

@Serializable
data class InvoiceModelLegacy(
    @Serializable(with = JavaUUIDSerializer::class) val id: UUID,
    val externalId: String,
    val senderCompanyName: String,
    val senderCompanyAddress: String,
    val recipientCompanyAddress: String,
    val recipientCompanyName: String,
    val issueDate: Instant,
    val dueDate: Instant,
    val createdAt: Instant,
    val updatedAt: Instant,
    val activities: List<InvoiceModelActivityModelLegacy>,
    val user: UserModel,
    val beneficiary: BeneficiaryModel,
    val intermediary: IntermediaryModel?
)

@Serializable
data class InvoiceModelActivityModelLegacy(
    @Serializable(with = JavaUUIDSerializer::class) val id: UUID,
    val name: String,
    val unitPrice: Long,
    val quantity: Int
)