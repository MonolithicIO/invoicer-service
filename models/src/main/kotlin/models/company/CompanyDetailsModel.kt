package models.company

import kotlinx.datetime.Instant
import models.paymentaccount.PaymentAccountModel
import java.util.*

data class CompanyDetailsModel(
    val name: String,
    val document: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val isDeleted: Boolean,
    val userId: UUID,
    val id: UUID,
    val address: CompanyAddressModel,
    val paymentAccount: PaymentAccountModel,
    val intermediaryAccount: PaymentAccountModel?
)
