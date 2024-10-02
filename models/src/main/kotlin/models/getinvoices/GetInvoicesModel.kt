package models.getinvoices

import kotlinx.datetime.LocalDate
import java.util.*

data class InvoiceListItemModel(
    val id: UUID,
    val externalId: String,
    val senderCompany: String,
    val recipientCompany: String,
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
    val totalAmount: Long,
)
