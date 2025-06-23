package models.getinvoices

import kotlinx.datetime.Instant
import java.util.*

data class GetInvoicesFilterModel(
    val minIssueDate: Instant?,
    val maxIssueDate: Instant?,
    val minDueDate: Instant?,
    val maxDueDate: Instant?,
    val customerId: UUID? = null
)
