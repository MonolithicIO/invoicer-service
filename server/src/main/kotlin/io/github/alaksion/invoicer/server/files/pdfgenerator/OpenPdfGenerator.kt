package io.github.alaksion.invoicer.server.files.pdfgenerator

import com.lowagie.text.Document
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfWriter
import io.github.alaksion.invoicer.server.data.entities.InvoiceEntity
import io.github.alaksion.invoicer.server.files.filehandler.FileHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

internal class OpenPdfGenerator(
    private val dispatcher: CoroutineDispatcher,
    private val fileHandler: FileHandler
) : PdfGenerator {

    override suspend fun generate(
        invoice: InvoiceEntity,
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
                document.add(Paragraph(invoice.id.toString()))
                document.close()
            }.onFailure {
                fileHandler.removeFile(file.absolutePath)
                throw it
            }
        }
        return file.absolutePath
    }

}