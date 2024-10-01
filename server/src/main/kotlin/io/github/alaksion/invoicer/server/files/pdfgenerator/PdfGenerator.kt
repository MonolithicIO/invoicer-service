package io.github.alaksion.invoicer.server.files.pdfgenerator

import services.api.model.InvoiceModel

typealias FilePath = String

internal interface PdfGenerator {
    suspend fun generate(
        invoice: InvoiceModel,
    ): FilePath

    companion object {
        const val tempPath = "pdfs"
    }
}