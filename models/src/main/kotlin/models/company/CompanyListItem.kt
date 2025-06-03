package models.company

data class CompanyList(
    val items: List<CompanyListItem>,
    val totalCount: Long,
    val nextPage: Long?
)

data class CompanyListItem(
    val name: String,
    val document: String
)
