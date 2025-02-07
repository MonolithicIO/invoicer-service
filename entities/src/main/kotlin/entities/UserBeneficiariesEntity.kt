package entities

data class UserBeneficiariesEntity(
    val items: List<BeneficiaryEntity>,
    val itemCount: Long,
    val nextPage: Long?
)
