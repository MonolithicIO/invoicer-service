package io.github.alaksion.invoicer.server.viewmodel.getinvoices

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class GetInvoicesFilterViewModel(
    val minIssueDate: LocalDate?,
    val maxIssueDate: LocalDate?,
    val minDueDate: LocalDate?,
    val maxDueDate: LocalDate?,
    val senderCompanyName: String?,
    val recipientCompanyName: String?,
)
