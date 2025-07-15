package io.github.monolithic.invoicer.services.pdf.pdfwriter

import io.github.monolithic.invoicer.models.invoice.InvoiceModel

internal interface InvoicePdfWriter {
    suspend fun write(invoice: InvoiceModel, outputPath: String)
}
