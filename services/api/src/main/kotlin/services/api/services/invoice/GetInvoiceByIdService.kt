package services.api.services.invoice

import models.InvoiceModel

interface GetInvoiceByIdService {
    suspend fun get(id: String, userId: String): InvoiceModel
}