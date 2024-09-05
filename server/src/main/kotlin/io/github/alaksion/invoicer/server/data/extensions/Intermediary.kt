package io.github.alaksion.invoicer.server.data.extensions

import entities.IntermediaryEntity
import io.github.alaksion.invoicer.server.domain.model.intermediary.IntermediaryModel


internal fun IntermediaryEntity.toModel(): IntermediaryModel = IntermediaryModel(
    name = this.name,
    iban = this.iban,
    swift = this.swift,
    bankName = this.bankName,
    bankAddress = this.bankAddress,
    userId = this.user.id.value.toString(),
    id = this.id.value.toString()
)