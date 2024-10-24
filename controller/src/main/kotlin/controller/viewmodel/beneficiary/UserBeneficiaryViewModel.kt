package controller.viewmodel.beneficiary

import kotlinx.serialization.Serializable
import models.beneficiary.BeneficiaryModel

@Serializable
internal data class UserBeneficiaryViewModel(
    val name: String,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
    val id: String,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
internal data class UserBeneficiariesViewModel(
    val beneficiaries: List<UserBeneficiaryViewModel>
)

internal fun BeneficiaryModel.toModel(): UserBeneficiaryViewModel {
    return UserBeneficiaryViewModel(
        name = name,
        iban = iban,
        swift = swift,
        bankName = bankName,
        bankAddress = bankAddress,
        id = id,
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}
