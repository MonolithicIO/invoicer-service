package services.api.services.pdf

interface InvoicePdfSecureLinkService {
    suspend fun generate(
        invoiceId: String,
        userId: String
    ): String
}