package models.company

data class CompanyAddressModel(
    val addressLine1: String,
    val addressLine2: String?,
    val city: String,
    val state: String,
    val postalCode: String,
    val countryCode: String, // ISO 3166-1 alpha-3 country code
)
