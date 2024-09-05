package domains.intermediary.data.api.extensions

import domains.intermediary.domain.api.model.IntermediaryModel
import entities.IntermediaryEntity

internal fun IntermediaryEntity.toModel(): IntermediaryModel = IntermediaryModel(
    name = this.name,
    iban = this.iban,
    swift = this.swift,
    bankName = this.bankName,
    bankAddress = this.bankAddress,
    userId = this.user.id.value.toString(),
    id = this.id.value.toString()
)