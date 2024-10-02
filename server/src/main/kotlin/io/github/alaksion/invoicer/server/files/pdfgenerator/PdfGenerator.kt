package io.github.alaksion.invoicer.server.files.pdfgenerator

import models.InvoiceModel

typealias FilePath = String

internal interface PdfGenerator {
    suspend fun generate(
        invoice: InvoiceModel,
    ): FilePath

    companion object {
        const val tempPath = "pdfs"
    }
}