package datasource.impl.mapper

import entities.BeneficiaryEntity
import models.beneficiary.BeneficiaryModel

internal fun BeneficiaryEntity.toModel(): BeneficiaryModel = BeneficiaryModel(
    name = this.name,
    iban = this.iban,
    swift = this.swift,
    bankName = this.bankName,
    bankAddress = this.bankAddress,
    userId = this.user.id.value.toString(),
    id = this.id.value.toString(),
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)