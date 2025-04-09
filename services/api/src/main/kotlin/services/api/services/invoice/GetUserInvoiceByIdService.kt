package services.api.services.invoice

import models.InvoiceModel
import java.util.*

interface GetUserInvoiceByIdService {
    suspend fun get(invoiceId: UUID, userId: UUID): InvoiceModel
}