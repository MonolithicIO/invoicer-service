package services.api.services.invoice

import models.createinvoice.CreateInvoiceResponseModel
import models.invoice.CreateInvoiceDTO
import java.util.*

interface CreateInvoiceService {
    suspend fun createInvoice(
        model: CreateInvoiceDTO,
        userId: UUID
    ): CreateInvoiceResponseModel
}
