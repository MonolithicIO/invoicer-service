package io.github.monolithic.invoicer.models.company

import java.util.*

data class UpdateCompanyAddressModel(
    val companyId: UUID,
    val addressLine: String,
    val addressLine2: String?,
    val city: String,
    val state: String,
    val postalCode: String
)
