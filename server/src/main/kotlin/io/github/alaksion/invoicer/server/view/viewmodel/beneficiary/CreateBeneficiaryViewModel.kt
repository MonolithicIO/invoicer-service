package io.github.alaksion.invoicer.server.view.viewmodel.beneficiary

import io.github.alaksion.invoicer.server.domain.model.beneficiary.CreateBeneficiaryModel
import kotlinx.serialization.Serializable
import utils.exceptions.badRequestError

@Serializable
data class CreateBeneficiaryViewModel(
    val name: String? = null,
    val swift: String? = null,
    val iban: String? = null,
    val bankName: String? = null,
    val bankAddress: String? = null
)

internal fun CreateBeneficiaryViewModel.toModel(): CreateBeneficiaryModel = CreateBeneficiaryModel(
    name = this.name?.trim() ?: badRequestError("Missing name field"),
    swift = this.swift?.trim() ?: badRequestError("Missing swift field"),
    bankName = this.bankName?.trim() ?: badRequestError("Missing bank name field"),
    bankAddress = this.bankAddress?.trim() ?: badRequestError("Missing bank address field"),
    iban = this.iban?.trim() ?: badRequestError("Missing iban field")
)

@Serializable
data class CreateBeneficiaryResponseViewModel(
    val id: String
)
