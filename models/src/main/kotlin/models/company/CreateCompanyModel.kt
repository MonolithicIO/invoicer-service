package models.company

data class CreateCompanyModel(
    val name: String,
    val document: String,
    val address: CreateCompanyAddressModel,
    val paymentAccount: CreateCompanyPaymentAccountModel,
    val intermediaryAccount: CreateCompanyPaymentAccountModel?,
)

data class CreateCompanyAddressModel(
    val addressLine1: String,
    val addressLine2: String?,
    val city: String,
    val state: String,
    val postalCode: String,
    val countryCode: String, // ISO 3166-1 alpha-3 country code
)

data class CreateCompanyPaymentAccountModel(
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
)
