package datasource.impl.mapper

import datasource.impl.entities.IntermediaryEntity
import models.intermediary.IntermediaryModel


internal fun IntermediaryEntity.toModel(): IntermediaryModel = IntermediaryModel(
    name = this.name,
    iban = this.iban,
    swift = this.swift,
    bankName = this.bankName,
    bankAddress = this.bankAddress,
    userId = this.user.id.value,
    id = this.id.value,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)