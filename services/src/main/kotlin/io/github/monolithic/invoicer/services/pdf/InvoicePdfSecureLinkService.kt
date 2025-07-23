package io.github.monolithic.invoicer.services.pdf

import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError
import io.github.monolithic.invoicer.foundation.exceptions.http.notFoundError
import io.github.monolithic.invoicer.foundation.storage.remote.SecureFileLinkGenerator
import io.github.monolithic.invoicer.models.invoicepdf.InvoicePdfStatus
import io.github.monolithic.invoicer.repository.InvoicePdfRepository
import io.github.monolithic.invoicer.services.invoice.GetUserInvoiceByIdService
import java.util.*

interface InvoicePdfSecureLinkService {
    suspend fun generate(
        invoiceId: UUID,
        userId: UUID,
        companyId: UUID
    ): String
}

internal class InvoicePdfSecureLinkServiceImpl(
    private val secureFileLinkGenerator: SecureFileLinkGenerator,
    private val getUserInvoiceByIdService: GetUserInvoiceByIdService,
    private val invoicePdfRepository: InvoicePdfRepository
) : InvoicePdfSecureLinkService {

    override suspend fun generate(
        invoiceId: UUID,
        userId: UUID,
        companyId: UUID
    ): String {
        getUserInvoiceByIdService.get(
            invoiceId = invoiceId,
            userId = userId,
            companyId = companyId
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
