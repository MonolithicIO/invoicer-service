package services.impl.pdf

import io.github.alaksion.invoicer.foundation.storage.FileDownloader
import models.invoicepdf.InvoicePdfStatus
import repository.api.repository.InvoicePdfRepository
import services.api.services.invoice.GetUserInvoiceByIdService
import services.api.services.pdf.DownloadInvoicePdfService
import utils.exceptions.notFoundError

internal class DownloadInvoicePdfServiceImpl(
    private val fileDownloader: FileDownloader,
    private val getUserInvoiceByIdService: GetUserInvoiceByIdService,
    private val invoicePdfRepository: InvoicePdfRepository
) : DownloadInvoicePdfService {

    override suspend fun download(invoiceId: String, userId: String) {
        getUserInvoiceByIdService.get(
            id = invoiceId,
            userId = userId
        )

        val invoicePdf = invoicePdfRepository.getInvoicePdf(invoiceId) ?: notFoundError("Invoice PDF not found")
        if (invoicePdf.status != InvoicePdfStatus.Success) notFoundError("Invoice PDF not found")

        runCatching {
            fileDownloader.downloadFile(invoicePdf.path)
        }.onFailure {
            throw it
        }.onSuccess { downloadPath ->
            println("File downloaded successfully: $downloadPath")
        }
    }
}