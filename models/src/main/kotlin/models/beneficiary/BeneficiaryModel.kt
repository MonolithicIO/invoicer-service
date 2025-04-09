package models.beneficiary

import io.github.alaksion.invoicer.utils.serialization.JavaUUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class BeneficiaryModel(
    val name: String,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
    val userId: String,
    @Serializable(with = JavaUUIDSerializer::class)
    val id: UUID,
    val createdAt: Instant,
    val updatedAt: Instant
)
