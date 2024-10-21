package services.api.services.invoice

import models.createinvoice.CreateInvoiceModel
import models.createinvoice.CreateInvoiceResponseModel

interface CreateInvoiceService {
    suspend fun createInvoice(
        model: CreateInvoiceModel,
        userId: String
    ): CreateInvoiceResponseModel
}