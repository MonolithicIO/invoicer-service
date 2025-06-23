package models.getinvoices

import kotlinx.datetime.Instant
import java.util.*

data class InvoiceListItemModelLegacy(
    val id: UUID,
    val externalId: String,
    val senderCompany: String,
    val recipientCompany: String,
    val issueDate: Instant,
    val dueDate: Instant,
    val createdAt: Instant,
    val updatedAt: Instant,
    val totalAmount: Long,
)

data class InvoiceListModelLegacy(
    val items: List<InvoiceListItemModelLegacy>,
    val totalResults: Long,
    val nextPage: Long?
)
