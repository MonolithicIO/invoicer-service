package io.github.monolithic.invoicer.processor.process

import java.util.*

sealed interface Process {
    data class InvoicePdfProcess(
        val invoiceId: UUID,
        val userId: UUID,
        val companyId: UUID,
    ) : Process
}