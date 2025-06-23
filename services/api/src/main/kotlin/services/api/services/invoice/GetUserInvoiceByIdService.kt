package services.api.services.invoice

import models.InvoiceModelLegacy
import java.util.*

interface GetUserInvoiceByIdService {
    suspend fun get(invoiceId: UUID, userId: UUID): InvoiceModelLegacy
}