package io.github.alaksion.invoicer.services.pdf.pdfwriter

import models.invoice.InvoiceModel

internal interface InvoicePdfWriter {
    suspend fun write(invoice: InvoiceModel, outputPath: String)
}
