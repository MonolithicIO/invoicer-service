package models.invoicepdf

import kotlinx.datetime.Instant
import java.util.*

data class InvoicePdfModel(
    val id: UUID,
    val invoiceId: UUID,
    val createdAt: Instant,
    val updatedAt: Instant,
    val path: String,
    val status: InvoicePdfStatus
)

enum class InvoicePdfStatus {
    Pending,
    Success,
    Failed
}
