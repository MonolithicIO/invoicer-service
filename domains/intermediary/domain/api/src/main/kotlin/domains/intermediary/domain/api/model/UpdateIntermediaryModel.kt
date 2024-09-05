package domains.intermediary.domain.api.model

data class UpdateIntermediaryModel(
    val name: String,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
)
