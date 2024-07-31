package io.github.alaksion.invoicer.server.domain.usecase

import io.github.alaksion.invoicer.server.data.entities.InvoicePDFStatus
import io.github.alaksion.invoicer.server.domain.errors.HttpError
import io.github.alaksion.invoicer.server.domain.model.InvoiceModel
import io.github.alaksion.invoicer.server.domain.repository.InvoicePdfRepository
import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.files.pdfgenerator.PdfGenerator
import io.ktor.http.*
import java.util.*

interface CreateInvoicePdfUseCase {
    suspend fun create(invoiceId: String)
}

internal class CreateInvoicePdfUseCaseImpl(
    private val invoiceRepository: InvoiceRepository,
    private val invoicePdfRepository: InvoicePdfRepository,
    private val pdfGenerator: PdfGenerator
) : CreateInvoicePdfUseCase {

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
            invoicePdfRepository.deleteInvoicePdf(UUID.fromString(pdf.id))
        }
    }

    private suspend fun getInvoice(id: UUID): InvoiceModel {
        return invoiceRepository.getInvoiceByUUID(id)
            ?: throw HttpError(
                statusCode = HttpStatusCode.NotFound,
                message = "Invoice not found"
            )
    }

}