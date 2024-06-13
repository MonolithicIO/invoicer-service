package invoicer.alaksiondev.com.files.pdfgenerator

import com.lowagie.text.Document
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfWriter
import invoicer.alaksiondev.com.files.filehandler.FileHandler
import invoicer.alaksiondev.com.models.InvoiceActivityModel
import invoicer.alaksiondev.com.models.InvoiceModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class OpenPdfGenerator(
    private val dispatcher: CoroutineDispatcher,
    private val fileHandler: FileHandler
) : PdfGenerator {

    override suspend fun generate(
        invoice: InvoiceModel,
        activities: List<InvoiceActivityModel>,
    ): FilePath {

        val file = File(
            fileHandler.createFile(
                path = PdfGenerator.tempPath,
                fileName = "${invoice.id}.pdf"
            )
        )

        withContext(dispatcher) {
            runCatching {
                val document = Document()
                PdfWriter.getInstance(document, FileOutputStream(file))
                document.open()
                document.add(Paragraph(invoice.id))
                document.close()
            }.onFailure {
                fileHandler.removeFile(file.absolutePath)
                throw it
            }
        }
        return file.absolutePath
    }

}