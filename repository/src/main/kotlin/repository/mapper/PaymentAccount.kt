package repository.mapper

import models.paymentaccount.PaymentAccountModel
import models.paymentaccount.PaymentAccountTypeModel
import repository.entities.PaymentAccountEntity

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
    updatedAt = updatedAt
)