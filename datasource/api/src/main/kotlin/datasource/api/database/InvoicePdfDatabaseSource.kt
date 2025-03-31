package datasource.api.database

import datasource.api.model.pdf.CreatePdfData
import models.invoicepdf.InvoicePdfModel

interface InvoicePdfDatabaseSource {
    suspend fun createPdf(
        payload: CreatePdfData
    )

    suspend fun getInvoicePdf(
        invoiceId: String
    ): InvoicePdfModel?
}