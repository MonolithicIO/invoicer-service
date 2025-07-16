package io.github.monolithic.invoicer.models.invoice

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import java.util.*

data class InvoiceListModel(
    val items: List<InvoiceListItemModel>,
    val nextPage: Long?,
    val totalCount: Long
)

data class InvoiceListItemModel(
    val id: UUID,
    val invoiceNumber: String,
    val companyName: String,
    val customerName: String,
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val createdAt: Instant,
    val updatedAt: Instant,
    val totalAmount: Long
)
