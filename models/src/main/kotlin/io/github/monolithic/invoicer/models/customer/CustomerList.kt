package io.github.monolithic.invoicer.models.customer

import kotlinx.datetime.Instant

data class CustomerList(
    val itemCount: Long,
    val nextPage: Long? = null,
    val items: List<CustomerListItem>
)

data class CustomerListItem(
    val id: String,
    val name: String,
    val email: String,
    val phone: String? = null,
    val companyId: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
