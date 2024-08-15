package io.github.alaksion.invoicer.server.view.viewmodel.intermediary

import io.github.alaksion.invoicer.server.domain.model.intermediary.CreateIntermediaryModel
import kotlinx.serialization.Serializable
import utils.exceptions.badRequestError

@Serializable
data class CreateIntermediaryViewModel(
    val name: String? = null,
    val swift: String? = null,
    val iban: String? = null,
    val bankName: String? = null,
    val bankAddress: String? = null
)

internal fun CreateIntermediaryViewModel.toModel(): CreateIntermediaryModel =
    CreateIntermediaryModel(
        name = this.name?.trim() ?: badRequestError("Missing name field"),
        swift = this.swift?.trim() ?: badRequestError("Missing swift field"),
        bankName = this.bankName?.trim() ?: badRequestError("Missing bank name field"),
        bankAddress = this.bankAddress?.trim() ?: badRequestError("Missing bank address field"),
        iban = this.iban?.trim() ?: badRequestError("Missing iban field")
    )

@Serializable
data class CreateIntermediaryResponseViewModel(
    val id: String
)
