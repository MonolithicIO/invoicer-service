package services.impl.pdf.pdfwriter

import models.InvoiceModelLegacy

internal interface InvoicePdfWriter {
    suspend fun write(invoice: InvoiceModelLegacy, outputPath: String)
}