package controller.viewmodel.beneficiary

import kotlinx.serialization.Serializable
import models.beneficiary.CreateBeneficiaryModel
import utils.exceptions.http.badRequestError

@Serializable
internal data class CreateBeneficiaryViewModel(
    val name: String? = null,
    val swift: String? = null,
    val iban: String? = null,
    val bankName: String? = null,
    val bankAddress: String? = null
)

internal fun CreateBeneficiaryViewModel.toViewModel(): CreateBeneficiaryModel = CreateBeneficiaryModel(
    name = this.name ?: badRequestError("Missing name field"),
    swift = this.swift ?: badRequestError("Missing swift field"),
    bankName = this.bankName ?: badRequestError("Missing bank name field"),
    bankAddress = this.bankAddress ?: badRequestError("Missing bank address field"),
    iban = this.iban ?: badRequestError("Missing iban field")
)

@Serializable
internal data class CreateBeneficiaryResponseViewModel(
    val id: String
)
