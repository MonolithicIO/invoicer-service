package invoicer.alaksiondev.com.repository

import invoicer.alaksiondev.com.datasource.InvoicePdfDataSource
import invoicer.alaksiondev.com.entities.InvoicePDFEntity
import invoicer.alaksiondev.com.entities.InvoicePDFStatus

interface InvoicePdfRepository {

    suspend fun generateInvoicePdf(invoiceId: String): String

    suspend fun updateInvoicePdf(
        path: String?,
        status: InvoicePDFStatus,
        pdfId: String,
    ): String

    suspend fun deleteInvoicePdf(pdfId: String): String

    suspend fun findPdfByInvoiceId(invoiceId: String): InvoicePDFEntity?

}

internal class InvoicePdfRepositoryImpl(
    private val dataSource: InvoicePdfDataSource
) : InvoicePdfRepository {

    override suspend fun generateInvoicePdf(invoiceId: String): String {
        return dataSource.generateInvoicePdf(invoiceId = invoiceId)
    }

    override suspend fun updateInvoicePdf(
        path: String?,
        status: InvoicePDFStatus,
        pdfId: String
    ): String {
        return dataSource.updateInvoicePdf(path = path, status = status, pdfId = pdfId)
    }

    override suspend fun deleteInvoicePdf(pdfId: String): String {
        return dataSource.deleteInvoicePdf(pdfId = pdfId)
    }

    override suspend fun findPdfByInvoiceId(invoiceId: String): InvoicePDFEntity? {
        TODO("Not yet implemented")
    }

}