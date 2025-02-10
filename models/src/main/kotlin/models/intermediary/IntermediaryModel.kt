package models.intermediary

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class IntermediaryModel(
    val name: String,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
    val userId: String,
    val id: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
