package invoicer.alaksiondev.com.domain.models.createinvoice

import kotlinx.serialization.Serializable

@Serializable
data class CreateInvoiceResponseModel(
    val invoiceId: String,
    val externalInvoiceId: String
)