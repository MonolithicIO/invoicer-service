package domains.intermediary.domain.api.model

data class IntermediaryModel(
    val name: String,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
    val userId: String,
    val id: String
)
