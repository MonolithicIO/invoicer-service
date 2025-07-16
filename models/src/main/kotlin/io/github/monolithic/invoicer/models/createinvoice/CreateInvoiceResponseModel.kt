package io.github.monolithic.invoicer.models.createinvoice

import java.util.UUID

data class CreateInvoiceResponseModel(
    val externalInvoiceId: String,
    val invoiceId: UUID
)
