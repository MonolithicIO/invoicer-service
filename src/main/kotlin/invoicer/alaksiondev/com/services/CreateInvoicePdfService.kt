package invoicer.alaksiondev.com.services

import invoicer.alaksiondev.com.errors.HttpError
import invoicer.alaksiondev.com.files.pdfgenerator.PdfGenerator
import invoicer.alaksiondev.com.models.InvoiceActivityModel
import invoicer.alaksiondev.com.models.InvoiceModel
import invoicer.alaksiondev.com.models.PdfStatusModel
import invoicer.alaksiondev.com.repository.InvoiceActivityRepository
import invoicer.alaksiondev.com.repository.InvoicePdfRepository
import invoicer.alaksiondev.com.repository.InvoiceRepository
import io.ktor.http.HttpStatusCode

interface CreateInvoicePdfService {
    suspend fun create(invoiceId: String)
}

class CreateInvoicePdfServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val invoiceActivityRepository: InvoiceActivityRepository,
    private val invoicePdfRepository: InvoicePdfRepository,
    private val pdfGenerator: PdfGenerator
) : CreateInvoicePdfService {

    override suspend fun create(invoiceId: String) {
        val invoice = getInvoice(invoiceId)
        val activities = getActivities(invoiceId)
        deleteExistingPdfIfExists(invoiceId)

        val newPdf = invoicePdfRepository.generateInvoicePdf(invoiceId)

        runCatching {
            pdfGenerator.generate(
                invoice = invoice,
                activities = activities,
            )
        }.fold(
            onFailure = {
                invoicePdfRepository.updateInvoicePdf(
                    path = null,
                    status = PdfStatusModel.Failed,
                    pdfId = newPdf
                )
                throw it
            },
            onSuccess = { filePath ->
                invoicePdfRepository.updateInvoicePdf(
                    path = filePath,
                    status = PdfStatusModel.Completed,
                    pdfId = newPdf
                )
            }
        )

    }

    private suspend fun deleteExistingPdfIfExists(invoiceId: String) {

        val existingPdf = invoicePdfRepository.findPdfByInvoiceId(invoiceId)
        existingPdf?.let { pdf ->
            invoicePdfRepository.deleteInvoicePdf(pdf.id)
        }
    }

    private suspend fun getInvoice(id: String): InvoiceModel {
        return invoiceRepository.getInvoiceById(id)
            ?: throw HttpError(
                statusCode = HttpStatusCode.NotFound,
                message = "Invoice not found"
            )
    }

    private suspend fun getActivities(invoiceId: String): List<InvoiceActivityModel> {
        return invoiceActivityRepository.getActivitiesByInvoiceId(invoiceId)
    }

}