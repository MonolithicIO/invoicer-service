package services.api.services.pdf

import java.util.*

interface GenerateInvoicePdfService {
    suspend fun generate(
        invoiceId: UUID,
        userId: UUID
    )
}