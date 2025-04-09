package controller.viewmodel.intermediary

import kotlinx.serialization.Serializable
import models.intermediary.CreateIntermediaryModel
import utils.exceptions.http.badRequestError

@Serializable
internal data class CreateIntermediaryViewModel(
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
internal data class CreateIntermediaryResponseViewModel(
    val id: String
)
