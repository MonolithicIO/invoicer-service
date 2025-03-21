package services.impl.pdf

import models.InvoiceModel
import services.api.services.pdf.GenerateInvoicePdfService
import services.api.services.pdf.PdfPath

internal class GenerateInvoicePdfServiceImpl: GenerateInvoicePdfService {

    override suspend fun generate(invoice: InvoiceModel): PdfPath {
        TODO("Not yet implemented")
    }
}