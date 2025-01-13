package models.getinvoices

import kotlinx.datetime.Instant
import java.util.*

data class InvoiceListItemModel(
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

data class InvoiceListModel(
    val items: List<InvoiceListItemModel>,
    val totalResults: Long,
    val nextPage: Long?
)
