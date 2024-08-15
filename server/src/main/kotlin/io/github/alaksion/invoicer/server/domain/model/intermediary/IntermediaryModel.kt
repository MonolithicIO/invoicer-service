package io.github.alaksion.invoicer.server.domain.model.intermediary

data class IntermediaryModel(
    val name: String,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
    val userId: String
)
