package services.api.services.invoice

import java.util.*
import models.invoice.InvoiceModel

interface GetUserInvoiceByIdService {
    suspend fun get(
        invoiceId: UUID,
        companyId: UUID,
        userId: UUID,
    ): InvoiceModel
}
