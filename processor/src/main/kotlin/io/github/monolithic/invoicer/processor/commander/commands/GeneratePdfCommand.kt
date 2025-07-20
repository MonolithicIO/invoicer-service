package io.github.monolithic.invoicer.processor.commander.commands

import io.github.monolithic.invoicer.processor.process.Process.InvoicePdfProcess
import io.github.monolithic.invoicer.services.pdf.GenerateInvoicePdfService

internal interface GeneratePdfCommand {
    suspend fun process(process: InvoicePdfProcess)
}

internal class GeneratePdfCommandImpl(
    private val invoicePdfService: GenerateInvoicePdfService
) : GeneratePdfCommand {

    override suspend fun process(process: InvoicePdfProcess) {
        invoicePdfService.generate(
            invoiceId = process.invoiceId,
            userId = process.userId,
            companyId = process.companyId
        )
    }
}
