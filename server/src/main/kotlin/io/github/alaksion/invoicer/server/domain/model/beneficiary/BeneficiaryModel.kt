package io.github.alaksion.invoicer.server.domain.model.beneficiary

import kotlinx.datetime.LocalDate

data class BeneficiaryModel(
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
