package io.github.monolithic.invoicer.repository

import io.github.monolithic.invoicer.models.invoicepdf.InvoicePdfModel
import io.github.monolithic.invoicer.models.invoicepdf.InvoicePdfStatus
import io.github.monolithic.invoicer.repository.datasource.InvoicePdfDataSource
import java.util.*

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
        return invoicePdfDataSource.getInvoicePdf(
            invoiceId = invoiceId
        )
    }
}
