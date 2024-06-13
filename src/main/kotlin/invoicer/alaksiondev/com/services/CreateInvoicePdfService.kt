package invoicer.alaksiondev.com.services

import invoicer.alaksiondev.com.entities.InvoicePDFStatus
import invoicer.alaksiondev.com.errors.HttpError
import invoicer.alaksiondev.com.repository.InvoicePdfRepository
import invoicer.alaksiondev.com.repository.InvoiceRepository
import io.ktor.http.HttpStatusCode

interface CreateInvoicePdfService {
    suspend fun create(invoiceId: String)
}

class CreateInvoicePdfServiceImpl(
    private val invoiceRepository: InvoiceRepository,
    private val invoicePdfRepository: InvoicePdfRepository,
) : CreateInvoicePdfService {

    override suspend fun create(invoiceId: String) {
        checkIfInvoiceExists(invoiceId)
        deleteExistingPdfIfExists(invoiceId)

        val newPdf = invoicePdfRepository.generateInvoicePdf(invoiceId)

        runCatching {

        }.onFailure {
            invoicePdfRepository.updateInvoicePdf(
                path = null,
                status = InvoicePDFStatus.failed,
                pdfId = newPdf
            )
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

    private suspend fun checkIfInvoiceExists(id: String) {
        invoiceRepository.getInvoiceById(id)
            ?: throw HttpError(
                statusCode = HttpStatusCode.NotFound,
                message = "Invoice not found"
            )
    }

}