package datasource.api.model.beneficiary

data class UpdateBeneficiaryData(
    val name: String?,
    val iban: String?,
    val swift: String?,
    val bankName: String?,
    val bankAddress: String?,
)

