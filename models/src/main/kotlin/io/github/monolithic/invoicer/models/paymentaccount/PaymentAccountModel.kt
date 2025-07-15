package io.github.monolithic.invoicer.models.paymentaccount

import kotlinx.datetime.Instant
import java.util.*

data class PaymentAccountModel(
    val id: UUID,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
    val companyId: UUID,
    val type: PaymentAccountTypeModel, // e.g., "primary" or "intermediary"
    val isDeleted: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant
)

enum class PaymentAccountTypeModel {
    Primary,
    Intermediary,
}
