package io.github.alaksion.invoicer.consumers.strategy

import io.github.alaksion.invoicer.consumers.messages.types.GeneratePdfMessage
import services.api.services.pdf.GenerateInvoicePdfService

internal interface GeneratePdfStrategy {
    suspend fun process(message: GeneratePdfMessage)
}

internal class GeneratePdfStrategyImpl(
    private val invoicePdfService: GenerateInvoicePdfService
) : GeneratePdfStrategy {

    override suspend fun process(message: GeneratePdfMessage) {
        invoicePdfService.generate(
            invoiceId = message.invoiceId,
            userId = message.userId
        )
    }
}
