package repository

import datasource.api.database.InvoicePdfDatabaseSource
import datasource.api.model.pdf.CreatePdfData
import models.invoicepdf.InvoicePdfModel
import models.invoicepdf.InvoicePdfStatus
import java.util.UUID

interface InvoicePdfRepository {
    suspend fun createInvoicePdf(
        invoiceId: UUID
    )

    suspend fun updateInvoicePdfState(
        invoiceId: UUID,
        status: InvoicePdfStatus,
        filePath: String,
    ): InvoicePdfModel

    suspend fun getInvoicePdf(
        invoiceId: UUID
    ): InvoicePdfModel?
}

internal class InvoicePdfRepositoryImpl(
    private val databaseSource: InvoicePdfDatabaseSource
) : InvoicePdfRepository {

    override suspend fun createInvoicePdf(invoiceId: UUID) {
        databaseSource.createPdf(
            payload = CreatePdfData(
                invoiceId = invoiceId,
                pdfPath = ""
            )
        )
    }

    override suspend fun updateInvoicePdfState(
        invoiceId: UUID,
        status: InvoicePdfStatus,
        filePath: String
    ): InvoicePdfModel {
        return databaseSource.updateInvoicePdfState(
            invoiceId = invoiceId,
            status = status,
            filePath = filePath
        )
    }

    override suspend fun getInvoicePdf(invoiceId: UUID): InvoicePdfModel? {
        return databaseSource.getInvoicePdf(invoiceId)
    }
}