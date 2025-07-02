package services.impl.pdf.pdfwriter.itext

import com.itextpdf.io.font.FontProgramFactory
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import models.invoice.InvoiceModel
import services.impl.pdf.pdfwriter.InvoicePdfWriter
import services.impl.pdf.pdfwriter.itext.components.PdfStyle.formatDate
import java.io.File
import java.io.FileOutputStream
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import services.impl.pdf.pdfwriter.itext.components.PdfStyle
import services.impl.pdf.pdfwriter.itext.components.buildHeader
import services.impl.pdf.pdfwriter.itext.components.invoicePdfActivities
import services.impl.pdf.pdfwriter.itext.components.invoicePdfFooter
import services.impl.pdf.pdfwriter.itext.components.invoicePdfPaymentInfo
import services.impl.pdf.pdfwriter.itext.components.invoicePdfRecipient

internal class ItextInvoiceWriter : InvoicePdfWriter {

    override suspend fun write(invoice: InvoiceModel, outputPath: String) {
        return suspendCoroutine { continuation ->
            runCatching {
                generateInvoicePdf(
                    invoice = invoice,
                    outputPath = outputPath
                )
            }.fold(
                onSuccess = {
                    continuation.resume(Unit)
                },
                onFailure = {
                    // Add proper logger
                    println(it)
                    continuation.resumeWithException(it)
                }
            )
        }
    }

    private fun generateInvoicePdf(invoice: InvoiceModel, outputPath: String) {
        // Configura o documento PDF
        val writer = PdfWriter(FileOutputStream(File(outputPath)))
        val pdf = PdfDocument(writer)
        val document = Document(pdf, PageSize.A4)

        val regular = PdfFontFactory.createFont(FontProgramFactory.createFont("Helvetica"))
        val bold = PdfFontFactory.createFont(FontProgramFactory.createFont("Helvetica-Bold"))

        document.setMargins(
            PdfStyle.Spacing.XLarge4,
            PdfStyle.Spacing.XLarge4,
            PdfStyle.Spacing.XLarge4,
            PdfStyle.Spacing.XLarge4
        )

        val headerTable = buildHeader(
            senderCompanyName = invoice.company.name,
            senderCompanyAddress = invoice.company.addressLine1,
            externalId = invoice.invoiceNumber,
            id = invoice.id.toString(),
            dueDate = formatDate(invoice.dueDate),
            issueDate = formatDate(invoice.issueDate),
            boldFont = bold,
            regularFont = regular
        )
        document.add(headerTable)

        document.add(Paragraph("\n"))

        val recipientTable = invoicePdfRecipient(
            recipientCompanyName = invoice.customer.name,
            boldFont = bold,
        )
        document.add(recipientTable)

        document.add(Paragraph("\n"))

        val activitiesTable = invoicePdfActivities(
            invoice.activities,
            boldFont = bold,
            regularFont = regular
        )
        document.add(activitiesTable)

        document.add(Paragraph("\n"))

        val paymentTable = invoicePdfPaymentInfo(
            primary = invoice.primaryAccount,
            intermediary = invoice.intermediaryAccount,
            boldFont = bold,
            regularFont = regular
        )

        document.add(paymentTable)

        document.add(Paragraph("\n"))

        val footerTable = invoicePdfFooter(
            updatedAt = invoice.updatedAt,
            createdAt = invoice.createdAt,
            regularFont = regular
        )
        document.add(footerTable)
        document.close()
    }
}
