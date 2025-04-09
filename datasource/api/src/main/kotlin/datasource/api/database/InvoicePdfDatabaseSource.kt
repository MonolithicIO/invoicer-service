package datasource.api.database

import datasource.api.model.pdf.CreatePdfData
import models.invoicepdf.InvoicePdfModel
import models.invoicepdf.InvoicePdfStatus
import java.util.*

interface InvoicePdfDatabaseSource {
    suspend fun createPdf(
        payload: CreatePdfData
    )

    suspend fun getInvoicePdf(
        invoiceId: UUID
    ): InvoicePdfModel?

    suspend fun updateInvoicePdfState(
        invoiceId: UUID,
        status: InvoicePdfStatus,
        filePath: String
    ): InvoicePdfModel
}