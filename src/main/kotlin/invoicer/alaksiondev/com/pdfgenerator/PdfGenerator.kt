package invoicer.alaksiondev.com.pdfgenerator

import invoicer.alaksiondev.com.models.InvoiceActivityModel
import invoicer.alaksiondev.com.models.InvoiceModel

typealias FilePath = String

interface PdfGenerator {
    suspend fun generate(
        invoice: InvoiceModel,
        activities: List<InvoiceActivityModel>,
    ): FilePath

    companion object {
        const val tempPath = "temp/pdf"
    }
}