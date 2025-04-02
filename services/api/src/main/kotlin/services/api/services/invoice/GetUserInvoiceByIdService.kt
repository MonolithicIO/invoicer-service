package services.api.services.invoice

import models.InvoiceModel

interface GetUserInvoiceByIdService {
    suspend fun get(id: String, userId: String): InvoiceModel
}