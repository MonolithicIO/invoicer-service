package models.invoice

import kotlinx.datetime.Instant
import java.util.*

data class InvoiceListModel(
    val items: List<InvoiceListItemModel>,
    val nextPage: Long?,
    val totalCount: Long
)

data class InvoiceListItemModel(
    val id: UUID,
    val invoiceNumber: String,
    val amount: Long,
    val companyName: String,
    val customerName: String,
    val issueDate: Instant,
    val dueDate: Instant,
    val createdAt: Instant,
    val updatedAt: Instant,
    val totalAmount: Long
)
