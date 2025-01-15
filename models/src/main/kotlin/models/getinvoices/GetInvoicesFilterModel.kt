package models.getinvoices

import kotlinx.datetime.Instant

data class GetInvoicesFilterModel(
    val minIssueDate: Instant?,
    val maxIssueDate: Instant?,
    val minDueDate: Instant?,
    val maxDueDate: Instant?,
    val senderCompanyName: String?,
    val recipientCompanyName: String?,
)
