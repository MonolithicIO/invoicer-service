package models.invoice

import java.util.*

data class InvoiceListModel(
    val items: List<InvoiceListItemModel>,
    val nextPage: Long?,
    val totalCount: Long
)

data class InvoiceListItemModel(
    val id: UUID,
    val invoiceNumber: String,
    val amount: Long
)
