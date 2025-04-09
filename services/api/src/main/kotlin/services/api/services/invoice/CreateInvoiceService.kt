package services.api.services.invoice

import models.createinvoice.CreateInvoiceModel
import models.createinvoice.CreateInvoiceResponseModel
import java.util.*

interface CreateInvoiceService {
    suspend fun createInvoice(
        model: CreateInvoiceModel,
        userId: UUID
    ): CreateInvoiceResponseModel
}