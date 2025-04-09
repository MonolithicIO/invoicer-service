package models.createinvoice

import kotlinx.datetime.Instant
import java.util.*

data class CreateInvoiceModel(
    val externalId: String,
    val senderCompanyName: String,
    val senderCompanyAddress: String,
    val recipientCompanyName: String,
    val recipientCompanyAddress: String,
    val issueDate: Instant,
    val dueDate: Instant,
    val beneficiaryId: UUID,
    val intermediaryId: UUID?,
    val activities: List<CreateInvoiceActivityModel>,
)


data class CreateInvoiceActivityModel(
    val description: String,
    val unitPrice: Long,
    val quantity: Int
)
