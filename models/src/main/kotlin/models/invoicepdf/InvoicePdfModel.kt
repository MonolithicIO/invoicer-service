package models.invoicepdf

import kotlinx.datetime.Instant

data class InvoicePdfModel(
    val id: String,
    val invoiceId: String,
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
