package services.api.services.invoice

import models.invoice.InvoiceModel
import java.util.*

interface GetUserInvoiceByIdService {
    suspend fun get(invoiceId: UUID): InvoiceModel?
}
