package invoicer.alaksiondev.com.models

import kotlinx.serialization.Serializable

@Serializable
data class InvoiceActivityModel(
    val description: String,
    val quantity: Int,
    val unitPrice: Long,
    val createdAt: String,
    val updatedAt: String,
    val invoice: InvoiceModel?
)