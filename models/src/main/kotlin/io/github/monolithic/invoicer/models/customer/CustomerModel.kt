package io.github.monolithic.invoicer.models.customer

import kotlinx.datetime.Instant
import java.util.*

data class CustomerModel(
    val id: UUID,
    val name: String,
    val email: String,
    val phone: String? = null,
    val companyId: UUID,
    val createdAt: Instant,
    val updatedAt: Instant
)
