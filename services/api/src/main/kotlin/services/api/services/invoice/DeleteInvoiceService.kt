package services.api.services.invoice

interface DeleteInvoiceService {
    suspend fun delete(
        invoiceId: String,
        userId: String
    )
}