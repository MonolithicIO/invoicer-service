package invoicer.alaksiondev.com.viewmodel.createinvoice

import kotlinx.serialization.Serializable

@Serializable
data class CreateInvoiceResponseViewModel(
    val invoiceId: String,
    val externalInvoiceId: String
)