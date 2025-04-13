package models.beneficiary

data class CreateBeneficiaryModel(
    val name: String,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
) {
    companion object {
        val teste = "123"
    }
}
