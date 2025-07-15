package io.github.monolithic.invoicer.models.company

import kotlinx.datetime.Instant
import java.util.*

data class CompanyModel(
    val name: String,
    val document: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val isDeleted: Boolean,
    val userId: UUID,
    val id: UUID
)
