package invoicer.alaksiondev.com.models

import kotlinx.serialization.Serializable

@Serializable
data class InvoicePdfModel(
    val id: String,
    val path: String,
    val status: PdfStatusModel,
    val invoiceId: String,
    val createdAt: String,
    val updatedAt: String,
)

@Serializable
enum class PdfStatusModel {
    Completed,
    Pending,
    Failed;
}
