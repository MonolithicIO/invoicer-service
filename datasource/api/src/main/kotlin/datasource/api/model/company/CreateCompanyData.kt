package datasource.api.model.company

data class CreateCompanyData(
    val name: String,
    val document: String,
    val address: CreateCompanyAddressData,
    val paymentAccount: CreateCompanyPaymentAccountData,
    val intermediaryAccount: CreateCompanyPaymentAccountData?,
)

data class CreateCompanyAddressData(
    val addressLine1: String,
    val addressLine2: String?,
    val city: String,
    val state: String,
    val postalCode: String,
    val countryCode: String, // ISO 3166-1 alpha-3 country code
)

data class CreateCompanyPaymentAccountData(
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
)