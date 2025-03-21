package services.impl.pdf.pdfwriter

import models.InvoiceModel

internal interface InvoicePdfWriter {
    suspend fun write(invoice: InvoiceModel, outputPath: String)
}