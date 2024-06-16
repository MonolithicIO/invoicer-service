package invoicer.alaksiondev.com.files.pdfgenerator

import invoicer.alaksiondev.com.entities.InvoiceEntity

typealias FilePath = String

internal interface PdfGenerator {
    suspend fun generate(
        invoice: InvoiceEntity,
    ): FilePath

    companion object {
        const val tempPath = "pdfs"
    }
}