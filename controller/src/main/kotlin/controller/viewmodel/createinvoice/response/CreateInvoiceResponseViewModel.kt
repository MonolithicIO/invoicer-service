package controller.viewmodel.createinvoice.response

import kotlinx.serialization.Serializable

@Serializable
internal data class CreateInvoiceResponseViewModel(
    val invoiceId: String,
    val externalInvoiceId: String
)