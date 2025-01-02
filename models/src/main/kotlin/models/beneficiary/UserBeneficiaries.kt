package models.beneficiary

data class UserBeneficiaries(
    val items: List<BeneficiaryModel>,
    val itemCount: Long,
    val nextPage: Long?
)
