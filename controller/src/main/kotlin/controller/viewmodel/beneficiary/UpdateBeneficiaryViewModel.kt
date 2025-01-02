package controller.viewmodel.beneficiary

import kotlinx.serialization.Serializable
import models.beneficiary.UpdateBeneficiaryModel
import utils.exceptions.badRequestError

@Serializable
internal data class UpdateBeneficiaryViewModel(
    val name: String? = null,
    val iban: String? = null,
    val swift: String? = null,
    val bankName: String? = null,
    val bankAddress: String? = null,
)

internal fun UpdateBeneficiaryViewModel.toViewModel(): UpdateBeneficiaryModel {
    return UpdateBeneficiaryModel(
        name = name?.trim() ?: badRequestError(message = "Missing field name"),
        iban = iban?.trim() ?: badRequestError(message = "Missing field iban"),
        swift = swift?.trim() ?: badRequestError(message = "Missing field swift"),
        bankName = bankName?.trim() ?: badRequestError(message = "Missing field bank name"),
        bankAddress = bankAddress?.trim() ?: badRequestError(message = "Missing field bank address"),
    )
}