package services.impl.pdf

import io.github.alaksion.invoicer.foundation.storage.remote.SecureFileLinkGenerator
import models.invoicepdf.InvoicePdfStatus
import repository.api.InvoicePdfRepository
import services.api.services.invoice.GetUserInvoiceByIdService
import services.api.services.pdf.InvoicePdfSecureLinkService
import utils.exceptions.http.badRequestError
import utils.exceptions.http.notFoundError
import java.util.UUID

internal class InvoicePdfSecureLinkServiceImpl(
    private val secureFileLinkGenerator: SecureFileLinkGenerator,
    private val getUserInvoiceByIdService: GetUserInvoiceByIdService,
    private val invoicePdfRepository: InvoicePdfRepository
) : InvoicePdfSecureLinkService {

    override suspend fun generate(invoiceId: UUID, userId: UUID): String {
        getUserInvoiceByIdService.get(
            invoiceId = invoiceId,
            userId = userId
        )

        val invoicePdf = invoicePdfRepository.getInvoicePdf(invoiceId) ?: notFoundError("Invoice PDF not found")
        if (invoicePdf.status != InvoicePdfStatus.Success) notFoundError("Invoice PDF not found")

        return runCatching {
            secureFileLinkGenerator.generateLink(
                fileKey = invoicePdf.path,
                durationInHours = 7
            )
        }.fold(
            onSuccess = { response ->
                response
            },
            onFailure = {
                badRequestError(it.message.orEmpty())
            }
        )
    }
}