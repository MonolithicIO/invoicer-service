package services.api.services.pdf

import models.InvoiceModel

typealias PdfPath = String

interface GenerateInvoicePdfService {
    suspend fun generate(invoice: InvoiceModel): PdfPath
}