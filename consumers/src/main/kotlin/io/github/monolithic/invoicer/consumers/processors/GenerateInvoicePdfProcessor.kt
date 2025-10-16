package io.github.monolithic.invoicer.consumers.processors

import io.github.monolithic.invoicer.consumers.messages.types.InvoicePdfMessage
import io.github.monolithic.invoicer.services.pdf.GenerateInvoicePdfService

internal interface GenerateInvoicePdfProcessor {
    suspend fun process(message: InvoicePdfMessage)
}

internal class GenerateInvoicePdfProcessorImpl(
    private val invoicePdfService: GenerateInvoicePdfService
) : GenerateInvoicePdfProcessor {

    override suspend fun process(message: InvoicePdfMessage) {
        invoicePdfService.generate(
            invoiceId = message.invoiceId,
            userId = message.userId,
            companyId = message.companyId
        )
    }
}
