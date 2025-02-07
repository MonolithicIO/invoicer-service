package datasource.api.model.invoice

import kotlinx.datetime.Instant

data class GetInvoicesFilterData(
    val minIssueDate: Instant?,
    val maxIssueDate: Instant?,
    val minDueDate: Instant?,
    val maxDueDate: Instant?,
    val senderCompanyName: String?,
    val recipientCompanyName: String?,
)
