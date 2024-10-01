package services.api.model.beneficiary

data class UpdateBeneficiaryModel(
    val name: String,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
)
