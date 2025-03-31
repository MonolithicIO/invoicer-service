package services.impl.pdf

import io.github.alaksion.invoicer.foundation.storage.FileUploader
import models.invoicepdf.InvoicePdfStatus
import repository.api.repository.InvoicePdfRepository
import services.api.services.invoice.GetInvoiceByIdService
import services.api.services.pdf.GenerateInvoicePdfService
import services.api.services.user.GetUserByIdService
import services.impl.pdf.pdfwriter.InvoicePdfWriter
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists

internal class GenerateInvoicePdfServiceImpl(
    private val getUserByIdService: GetUserByIdService,
    private val getInvoiceByIdService: GetInvoiceByIdService,
    private val writer: InvoicePdfWriter,
    private val fileUploader: FileUploader,
    private val invoicePdfRepository: InvoicePdfRepository
) : GenerateInvoicePdfService {

    override suspend fun generate(invoiceId: String, userId: String) {
        getUserByIdService.get(userId)

        val invoice = getInvoiceByIdService.get(
            userId = userId,
            id = invoiceId
        )

        invoicePdfRepository.createInvoicePdf(invoiceId)

        val outputPath = Path("")
            .toAbsolutePath()
            .toString() + "/temp/pdfs" + "/invoice-${invoice.id}.pdf"

        writer.write(
            invoice = invoice,
            outputPath = outputPath
        )

        runCatching {
            fileUploader.uploadFile(
                localFilePath = outputPath,
                fileName = "user/$userId/$invoiceId.pdf"
            )
        }.fold(
            onFailure = {
                invoicePdfRepository.updateInvoicePdfState(
                    invoiceId = invoiceId,
                    status = InvoicePdfStatus.Failed,
                    filePath = ""
                )
                throw it
            },
            onSuccess = { fileKey ->
                invoicePdfRepository.updateInvoicePdfState(
                    invoiceId = invoiceId,
                    status = InvoicePdfStatus.Success,
                    filePath = fileKey
                )
            }
        )

        Path(outputPath).deleteIfExists()
    }
}