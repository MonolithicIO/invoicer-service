package controller.viewmodel.invoice

import kotlinx.serialization.Serializable

@Serializable
internal data class CreateInvoiceResponseViewModel(
    val invoiceId: String,
    val externalInvoiceId: String
)