package controller.viewmodel.beneficiary

import kotlinx.serialization.Serializable
import models.beneficiary.BeneficiaryModel
import models.beneficiary.UserBeneficiaries

@Serializable
internal data class UserBeneficiariesViewModel(
    val itemCount: Long,
    val nextPage: Long?,
    val items: List<UserBeneficiaryViewModel>
)

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

internal fun UserBeneficiaries.toViewModel() = UserBeneficiariesViewModel(
    items = this.items.map { it.toViewModel() },
    itemCount = this.itemCount,
    nextPage = this.nextPage
)

internal fun BeneficiaryModel.toViewModel(): UserBeneficiaryViewModel {
    return UserBeneficiaryViewModel(
        name = name,
        iban = iban,
        swift = swift,
        bankName = bankName,
        bankAddress = bankAddress,
        id = id.toString(),
        createdAt = createdAt.toString(),
        updatedAt = updatedAt.toString()
    )
}
