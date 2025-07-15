package io.github.monolithic.invoicer.repository.mapper

import io.github.monolithic.invoicer.models.paymentaccount.PaymentAccountModel
import io.github.monolithic.invoicer.models.paymentaccount.PaymentAccountTypeModel
import io.github.monolithic.invoicer.repository.entities.PaymentAccountEntity

internal fun PaymentAccountEntity.toModel() = PaymentAccountModel(
    iban = iban,
    swift = swift,
    bankName = bankName,
    bankAddress = bankAddress,
    type = when (type) {
        "primary" -> PaymentAccountTypeModel.Primary
        "intermediary" -> PaymentAccountTypeModel.Intermediary
        else -> throw IllegalArgumentException("Unknown payment account type: $type")
    },
    isDeleted = isDeleted,
    createdAt = createdAt,
    updatedAt = updatedAt,
    companyId = company.id.value,
    id = id.value
)
