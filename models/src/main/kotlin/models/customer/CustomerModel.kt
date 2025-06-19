package models.customer

import kotlinx.datetime.Instant

data class CustomerModel(
    val id: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val companyId: String,
    val createdAt: Instant,
    val updatedAt: Instant
)