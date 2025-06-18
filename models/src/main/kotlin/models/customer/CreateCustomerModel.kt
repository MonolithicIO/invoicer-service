package models.customer

import java.util.*

data class CreateCustomerModel(
    val name: String,
    val email: String,
    val phone: String,
    val companyId: UUID
)
