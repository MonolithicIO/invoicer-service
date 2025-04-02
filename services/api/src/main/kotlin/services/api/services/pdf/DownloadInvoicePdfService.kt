package services.api.services.pdf

interface DownloadInvoicePdfService {
    suspend fun download(
        invoiceId: String,
        userId: String
    )
}