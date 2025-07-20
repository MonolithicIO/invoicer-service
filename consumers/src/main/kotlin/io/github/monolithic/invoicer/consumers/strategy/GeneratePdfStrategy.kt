package io.github.monolithic.invoicer.consumers.strategy

import io.github.monolithic.invoicer.consumers.messages.types.GeneratePdfMessage
import io.github.monolithic.invoicer.services.pdf.GenerateInvoicePdfService

internal interface GeneratePdfStrategy {
    suspend fun process(message: GeneratePdfMessage)
}

internal class GeneratePdfStrategyImpl(
    private val invoicePdfService: GenerateInvoicePdfService
) : GeneratePdfStrategy {

    override suspend fun process(message: GeneratePdfMessage) {
        invoicePdfService.generate(
            invoiceId = message.invoiceId,
            userId = message.userId,
            companyId = message.companyId
        )
    }
}
