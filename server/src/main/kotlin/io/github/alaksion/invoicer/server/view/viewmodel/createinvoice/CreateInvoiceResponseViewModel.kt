package io.github.alaksion.invoicer.server.view.viewmodel.createinvoice

import kotlinx.serialization.Serializable

@Serializable
data class CreateInvoiceResponseViewModel(
    val invoiceId: String,
    val externalInvoiceId: String
)