package invoicer.alaksiondev.com.pdfgenerator

import com.lowagie.text.Document
import com.lowagie.text.Paragraph
import com.lowagie.text.html.HtmlWriter
import invoicer.alaksiondev.com.models.InvoiceActivityModel
import invoicer.alaksiondev.com.models.InvoiceModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.FileOutputStream

class OpenPdfGenerator(
    private val dispatcher: CoroutineDispatcher
) : PdfGenerator {

    override suspend fun generate(
        invoice: InvoiceModel,
        activities: List<InvoiceActivityModel>,
    ): FilePath {
        val filePath = "${PdfGenerator.tempPath}/temp_invoice_${invoice.id}.html"
        withContext(dispatcher) {
            val document = Document()
            HtmlWriter.getInstance(
                document,
                FileOutputStream(filePath)
            )
            document.open()
            document.add(Paragraph(invoice.id))
            document.close()
        }
        return filePath
    }

}