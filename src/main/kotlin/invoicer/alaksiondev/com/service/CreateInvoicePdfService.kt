package invoicer.alaksiondev.com.service

import invoicer.alaksiondev.com.entities.InvoiceEntity
import invoicer.alaksiondev.com.entities.InvoicePDFStatus
import invoicer.alaksiondev.com.errors.HttpError
import invoicer.alaksiondev.com.files.pdfgenerator.PdfGenerator
import invoicer.alaksiondev.com.repository.InvoicePdfRepository
import invoicer.alaksiondev.com.repository.InvoiceRepository
import io.ktor.http.*
import java.util.*

interface CreateInvoicePdfService {
    suspend fun create(invoiceId: String)
}

internal class CreateInvoicePdfServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val invoicePdfRepository: InvoicePdfRepository,
    private val pdfGenerator: PdfGenerator
) : CreateInvoicePdfService {

    override suspend fun create(invoiceId: String) {
        val parsedId = UUID.fromString(invoiceId)
        val invoice = getInvoice(parsedId)
        deleteExistingPdfIfExists(parsedId)

        val newPdf = invoicePdfRepository.generateInvoicePdf(invoiceId)

        runCatching {
            pdfGenerator.generate(
                invoice = invoice,
            )
        }.fold(
            onFailure = {
                invoicePdfRepository.updateInvoicePdf(
                    path = null,
                    status = InvoicePDFStatus.failed,
                    pdfId = newPdf
                )
                throw it
            },
            onSuccess = { filePath ->
                invoicePdfRepository.updateInvoicePdf(
                    path = filePath,
                    status = InvoicePDFStatus.ok,
                    pdfId = newPdf
                )
            }
        )

    }

    private suspend fun deleteExistingPdfIfExists(invoiceId: UUID) {
        val existingPdf = invoicePdfRepository.findPdfByInvoiceId(invoiceId)
        existingPdf?.let { pdf ->
            invoicePdfRepository.deleteInvoicePdf(pdf.id.value)
        }
    }

    private suspend fun getInvoice(id: UUID): InvoiceEntity {
        return invoiceRepository.getInvoiceById(id, eagerLoadActivities = true)
            ?: throw HttpError(
                statusCode = HttpStatusCode.NotFound,
                message = "Invoice not found"
            )
    }

}