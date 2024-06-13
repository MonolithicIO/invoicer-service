package invoicer.alaksiondev.com.services

import invoicer.alaksiondev.com.errors.HttpError
import invoicer.alaksiondev.com.models.InvoiceActivityModel
import invoicer.alaksiondev.com.models.InvoiceModel
import invoicer.alaksiondev.com.pdfgenerator.PdfGenerator
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
//        deleteExistingPdfIfExists(invoiceId)
//
//        val newPdf = invoicePdfRepository.generateInvoicePdf(invoiceId)

        runCatching {
            pdfGenerator.generate(
                invoice = invoice,
                activities = activities,
            )
        }.onFailure {
//            invoicePdfRepository.updateInvoicePdf(
//                path = null,
//                status = InvoicePDFStatus.failed,
//                pdfId = newPdf
//            )
        }

        // call pdf generator, generate file, store file in temp directory
        // save file, get path, update path column, update status column
        // end service
        // on exception set pdf status to error, end call

    }

    private suspend fun deleteExistingPdfIfExists(invoiceId: String) {

        val existingPdf = invoicePdfRepository.findPdfByInvoiceId(invoiceId)
        existingPdf?.let { pdf ->
            invoicePdfRepository.deleteInvoicePdf(pdf.id.toString())
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