package models.paymentaccount

import kotlinx.datetime.Instant

data class PaymentAccountModel(
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
    val type: PaymentAccountTypeModel, // e.g., "primary" or "intermediary"
    val isDeleted: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant
)

enum class PaymentAccountTypeModel {
    Primary,
    Intermediary,
}