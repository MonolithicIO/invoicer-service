package services.impl.pdf

import io.github.alaksion.invoicer.foundation.storage.FileUploader
import services.api.services.invoice.GetInvoiceByIdService
import services.api.services.pdf.GenerateInvoicePdfService
import services.api.services.pdf.PdfPath
import services.api.services.user.GetUserByIdService
import services.impl.pdf.pdfwriter.InvoicePdfWriter
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists

internal class GenerateInvoicePdfServiceImpl(
    private val getUserByIdService: GetUserByIdService,
    private val getInvoiceByIdService: GetInvoiceByIdService,
    private val writer: InvoicePdfWriter,
    private val fileUploader: FileUploader
) : GenerateInvoicePdfService {

    override suspend fun generate(invoiceId: String, userId: String): PdfPath {
        getUserByIdService.get(userId)

        val invoice = getInvoiceByIdService.get(
            userId = userId,
            id = invoiceId
        )

        val outputPath = Path("")
            .toAbsolutePath()
            .toString() + "/temp/pdfs" + "/invoice-${invoice.id}.pdf"

        writer.write(
            invoice = invoice,
            outputPath = outputPath
        )

        val fileKey = runCatching {
            fileUploader.uploadFile(
                localFilePath = outputPath,
                fileName = "user/$userId/$invoiceId.pdf"
            )
        }.fold(
            onFailure = {
                throw it
            },
            onSuccess = { response ->
                response
            }
        )

        Path(outputPath).deleteIfExists()

        return fileKey
    }
}