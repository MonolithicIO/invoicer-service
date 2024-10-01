package services.api.model.intermediary

import kotlinx.datetime.LocalDate

data class IntermediaryModel(
    val name: String,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
    val userId: String,
    val id: String,
    val createdAt: LocalDate,
    val updatedAt: LocalDate
)
