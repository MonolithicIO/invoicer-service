package io.github.monolithic.invoicer.models.company

import java.util.UUID

data class CompanyList(
    val items: List<CompanyListItem>,
    val totalCount: Long,
    val nextPage: Long?
)

data class CompanyListItem(
    val id: UUID,
    val name: String,
    val document: String
)
