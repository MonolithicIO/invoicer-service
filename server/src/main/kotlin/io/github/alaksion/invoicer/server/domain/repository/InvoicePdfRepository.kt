package io.github.alaksion.invoicer.server.domain.repository

import io.github.alaksion.invoicer.server.data.entities.InvoicePDFStatus
import io.github.alaksion.invoicer.server.domain.model.pdf.InvoicePdfModel
import java.util.*

interface InvoicePdfRepository {
    suspend fun generateInvoicePdf(invoiceId: String): String

    suspend fun updateInvoicePdf(
        path: String?,
        status: InvoicePDFStatus,
        pdfId: String,
    ): InvoicePdfModel?

    suspend fun deleteInvoicePdf(pdfId: UUID)

    suspend fun findPdfByInvoiceId(invoiceId: UUID): InvoicePdfModel?
}