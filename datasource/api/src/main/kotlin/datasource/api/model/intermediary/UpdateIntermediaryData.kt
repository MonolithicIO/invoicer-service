package datasource.api.model.intermediary

data class UpdateIntermediaryData(
    val name: String?,
    val iban: String?,
    val swift: String?,
    val bankName: String?,
    val bankAddress: String?,
)

