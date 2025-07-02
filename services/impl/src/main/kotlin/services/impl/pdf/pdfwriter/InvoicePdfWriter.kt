package services.impl.pdf.pdfwriter

import models.invoice.InvoiceModel

internal interface InvoicePdfWriter {
    suspend fun write(invoice: InvoiceModel, outputPath: String)
}
