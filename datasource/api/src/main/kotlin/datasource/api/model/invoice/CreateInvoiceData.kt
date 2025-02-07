package datasource.api.model.invoice

import kotlinx.datetime.Instant

data class CreateInvoiceData(
    val externalId: String,
    val senderCompanyName: String,
    val senderCompanyAddress: String,
    val recipientCompanyName: String,
    val recipientCompanyAddress: String,
    val issueDate: Instant,
    val dueDate: Instant,
    val beneficiaryId: String,
    val intermediaryId: String?,
    val activities: List<CreateInvoiceActivityData>,
)


data class CreateInvoiceActivityData(
    val description: String,
    val unitPrice: Long,
    val quantity: Int
)
