package datasource.api.database

import datasource.api.model.pdf.CreatePdfData
import models.invoicepdf.InvoicePdfModel
import models.invoicepdf.InvoicePdfStatus

interface InvoicePdfDatabaseSource {
    suspend fun createPdf(
        payload: CreatePdfData
    )

    suspend fun getInvoicePdf(
        invoiceId: String
    ): InvoicePdfModel?

    suspend fun updateInvoicePdfState(
        invoiceId: String,
        status: InvoicePdfStatus,
        filePath: String
    ): InvoicePdfModel
}