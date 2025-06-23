package services.impl.invoice

import models.invoice.InvoiceModel
import repository.InvoiceRepository
import services.api.services.invoice.GetUserInvoiceByIdService
import java.util.*

internal class GetUserInvoiceByIdServiceImpl(
    private val repository: InvoiceRepository
) : GetUserInvoiceByIdService {

    override suspend fun get(invoiceId: UUID): InvoiceModel? {
        return repository.getById(invoiceId)
    }
}