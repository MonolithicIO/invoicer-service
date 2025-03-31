package repository.api.repository

import datasource.api.database.InvoicePdfDatabaseSource
import datasource.api.model.pdf.CreatePdfData
import models.invoicepdf.InvoicePdfModel
import models.invoicepdf.InvoicePdfStatus

interface InvoicePdfRepository {
    suspend fun createInvoicePdf(
        invoiceId: String
    )

    suspend fun updateInvoicePdfState(
        invoiceId: String,
        status: InvoicePdfStatus,
        filePath: String,
    ): InvoicePdfModel
}

internal class InvoicePdfRepositoryImpl(
    private val databaseSource: InvoicePdfDatabaseSource
) : InvoicePdfRepository {

    override suspend fun createInvoicePdf(invoiceId: String) {
        databaseSource.createPdf(
            payload = CreatePdfData(
                invoiceId = invoiceId,
                pdfPath = ""
            )
        )
    }

    override suspend fun updateInvoicePdfState(
        invoiceId: String,
        status: InvoicePdfStatus,
        filePath: String
    ): InvoicePdfModel {
        return databaseSource.updateInvoicePdfState(
            invoiceId = invoiceId,
            status = status,
            filePath = filePath
        )
    }
}