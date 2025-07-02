package services.api.services.invoice

import java.util.*

interface DeleteInvoiceService {
    suspend fun delete(
        invoiceId: UUID,
        userId: UUID
    )
}
