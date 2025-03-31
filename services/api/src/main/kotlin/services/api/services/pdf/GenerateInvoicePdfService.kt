package services.api.services.pdf

interface GenerateInvoicePdfService {
    suspend fun generate(
        invoiceId: String,
        userId: String
    )
}