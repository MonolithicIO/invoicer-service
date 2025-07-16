package io.github.monolithic.invoicer.services.pdf

import io.github.monolithic.invoicer.foundation.storage.remote.SecureFileLinkGenerator
import java.util.UUID
import io.github.monolithic.invoicer.repository.InvoicePdfRepository
import io.github.monolithic.invoicer.services.invoice.GetUserInvoiceByIdService

interface InvoicePdfSecureLinkService {
    suspend fun generate(
        invoiceId: UUID,
        userId: UUID
    ): String
}

@Suppress("All")
internal class InvoicePdfSecureLinkServiceImpl(
    private val secureFileLinkGenerator: SecureFileLinkGenerator,
    private val getUserInvoiceByIdService: GetUserInvoiceByIdService,
    private val invoicePdfRepository: InvoicePdfRepository
) : InvoicePdfSecureLinkService {

    override suspend fun generate(invoiceId: UUID, userId: UUID): String {
//        getUserInvoiceByIdService.get(
//            invoiceId = invoiceId,
//            userId = userId
//        )
//
//        val invoicePdf = invoicePdfRepository.getInvoicePdf(invoiceId) ?: notFoundError("Invoice PDF not found")
//        if (invoicePdf.status != InvoicePdfStatus.Success) notFoundError("Invoice PDF not found")
//
//        return runCatching {
//            secureFileLinkGenerator.generateLink(
//                fileKey = invoicePdf.path,
//                durationInHours = 7
//            )
//        }.fold(
//            onSuccess = { response ->
//                response
//            },
//            onFailure = {
//                badRequestError(it.message.orEmpty())
//            }
//        )
        return ""
    }
}
