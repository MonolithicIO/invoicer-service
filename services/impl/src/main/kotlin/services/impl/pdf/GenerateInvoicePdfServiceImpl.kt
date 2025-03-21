package services.impl.pdf

import services.api.services.invoice.GetInvoiceByIdService
import services.api.services.pdf.GenerateInvoicePdfService
import services.api.services.pdf.PdfPath
import services.api.services.user.GetUserByIdService
import services.impl.pdf.pdfwriter.InvoicePdfWriter
import kotlin.io.path.Path

internal class GenerateInvoicePdfServiceImpl(
    private val getUserByIdService: GetUserByIdService,
    private val getInvoiceByIdService: GetInvoiceByIdService,
    private val writer: InvoicePdfWriter
) : GenerateInvoicePdfService {

    override suspend fun generate(invoiceId: String, userId: String): PdfPath {
        getUserByIdService.get(userId)

        val invoice = getInvoiceByIdService.get(
            userId = userId,
            id = invoiceId
        )

        val outputPath = Path("").toAbsolutePath().toString()+"/invoice-${invoice.id}.pdf"

        writer.write(
            invoice = invoice,
            outputPath = outputPath
        )

        return outputPath
    }
}