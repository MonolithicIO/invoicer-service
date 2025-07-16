package io.github.monolithic.invoicer.models.paymentaccount

import java.util.*

data class UpdatePaymentAccountModel(
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
    val id: UUID
)
