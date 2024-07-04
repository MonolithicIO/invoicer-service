package io.github.alaksion.invoicer.server.files.pdfgenerator

import io.github.alaksion.invoicer.server.data.entities.InvoiceEntity

typealias FilePath = String

internal interface PdfGenerator {
    suspend fun generate(
        invoice: InvoiceEntity,
    ): FilePath

    companion object {
        const val tempPath = "pdfs"
    }
}