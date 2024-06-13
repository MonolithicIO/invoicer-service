package invoicer.alaksiondev.com.repository

import invoicer.alaksiondev.com.datasource.InvoicePdfDataSource
import invoicer.alaksiondev.com.entities.fromDomain
import invoicer.alaksiondev.com.entities.toDomain
import invoicer.alaksiondev.com.models.InvoicePdfModel
import invoicer.alaksiondev.com.models.PdfStatusModel

interface InvoicePdfRepository {

    suspend fun generateInvoicePdf(invoiceId: String): String

    suspend fun updateInvoicePdf(
        path: String?,
        status: PdfStatusModel,
        pdfId: String,
    ): String

    suspend fun deleteInvoicePdf(pdfId: String): String

    suspend fun findPdfByInvoiceId(invoiceId: String): InvoicePdfModel?

}

internal class InvoicePdfRepositoryImpl(
    private val dataSource: InvoicePdfDataSource
) : InvoicePdfRepository {

    override suspend fun generateInvoicePdf(invoiceId: String): String {
        return dataSource.generateInvoicePdf(invoiceId = invoiceId)
    }

    override suspend fun updateInvoicePdf(
        path: String?,
        status: PdfStatusModel,
        pdfId: String
    ): String {
        return dataSource.updateInvoicePdf(path = path, status = status.fromDomain(), pdfId = pdfId)
    }

    override suspend fun deleteInvoicePdf(pdfId: String): String {
        return dataSource.deleteInvoicePdf(pdfId = pdfId)
    }

    override suspend fun findPdfByInvoiceId(invoiceId: String): InvoicePdfModel? {
        return dataSource.findPdfByInvoiceId(invoiceId)?.toDomain()
    }

}