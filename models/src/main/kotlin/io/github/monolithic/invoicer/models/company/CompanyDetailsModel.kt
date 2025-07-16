package io.github.monolithic.invoicer.models.company

import io.github.monolithic.invoicer.models.paymentaccount.PaymentAccountModel
import io.github.monolithic.invoicer.models.user.UserModel
import java.util.*
import kotlinx.datetime.Instant

data class CompanyDetailsModel(
    val name: String,
    val document: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val isDeleted: Boolean,
    val id: UUID,
    val address: CompanyAddressModel,
    val paymentAccount: PaymentAccountModel,
    val intermediaryAccount: PaymentAccountModel?,
    val user: UserModel
)
