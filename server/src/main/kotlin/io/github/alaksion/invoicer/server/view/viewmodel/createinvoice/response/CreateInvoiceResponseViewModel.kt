package io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.response

import kotlinx.serialization.Serializable

@Serializable
data class CreateInvoiceResponseViewModel(
    val invoiceId: String,
    val externalInvoiceId: String
)