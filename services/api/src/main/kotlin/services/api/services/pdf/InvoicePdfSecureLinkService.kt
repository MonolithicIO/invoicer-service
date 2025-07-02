package services.api.services.pdf

import java.util.UUID

interface InvoicePdfSecureLinkService {
    suspend fun generate(
        invoiceId: UUID,
        userId: UUID
    ): String
}
