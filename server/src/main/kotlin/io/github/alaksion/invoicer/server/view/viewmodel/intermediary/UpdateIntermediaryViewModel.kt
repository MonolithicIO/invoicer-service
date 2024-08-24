package io.github.alaksion.invoicer.server.view.viewmodel.intermediary

import io.github.alaksion.invoicer.server.domain.model.intermediary.UpdateIntermediaryModel
import kotlinx.serialization.Serializable
import utils.exceptions.badRequestError

@Serializable
internal data class UpdateIntermediaryViewModel(
    val name: String? = null,
    val iban: String? = null,
    val swift: String? = null,
    val bankName: String? = null,
    val bankAddress: String? = null,
)

internal fun UpdateIntermediaryViewModel.toModel(): UpdateIntermediaryModel {
    return UpdateIntermediaryModel(
        name = name?.trim() ?: badRequestError(message = "Missing field name"),
        iban = iban?.trim() ?: badRequestError(message = "Missing field iban"),
        swift = swift?.trim() ?: badRequestError(message = "Missing field swift"),
        bankName = bankName?.trim() ?: badRequestError(message = "Missing field bank name"),
        bankAddress = bankAddress?.trim() ?: badRequestError(message = "Missing field bank address"),
    )
}