package controller.viewmodel.intermediary

import kotlinx.serialization.Serializable
import models.intermediary.IntermediaryModel

@Serializable
internal data class UserIntermediaryViewModel(
    val name: String,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
    val id: String,
    val createdAt: String,
    val updatedAt: String
)

internal fun IntermediaryModel.toViewModel() = UserIntermediaryViewModel(
    name = name,
    iban = iban,
    swift = swift,
    bankName = bankName,
    bankAddress = bankAddress,
    id = this.id,
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString()
)

@Serializable
internal data class UserIntermediariesViewModel(
    val intermediaries: List<UserIntermediaryViewModel>
)
