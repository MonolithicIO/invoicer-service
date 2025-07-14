package repository

import java.util.*
import models.invoicepdf.InvoicePdfModel
import models.invoicepdf.InvoicePdfStatus
import repository.datasource.InvoicePdfDataSource

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
    private val invoicePdfDataSource: InvoicePdfDataSource
) : InvoicePdfRepository {

    override suspend fun createInvoicePdf(invoiceId: UUID) {
        return invoicePdfDataSource.createInvoicePdf(invoiceId = invoiceId)
    }

    override suspend fun updateInvoicePdfState(
        invoiceId: UUID,
        status: InvoicePdfStatus,
        filePath: String
    ): InvoicePdfModel {
        return invoicePdfDataSource.updateInvoicePdfState(
            invoiceId = invoiceId,
            status = status,
            filePath = filePath
        )
    }

    override suspend fun getInvoicePdf(invoiceId: UUID): InvoicePdfModel? {
        return getInvoicePdf(
            invoiceId = invoiceId
        )
    }
}
