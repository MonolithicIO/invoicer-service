package services.api.services.pdf

typealias PdfPath = String

interface GenerateInvoicePdfService {
    suspend fun generate(
        invoiceId: String,
        userId: String
    ): PdfPath
}