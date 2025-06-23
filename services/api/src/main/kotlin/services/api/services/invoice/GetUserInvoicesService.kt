package services.api.services.invoice

import models.getinvoices.GetInvoicesFilterModel
import models.getinvoices.InvoiceListModelLegacy
import java.util.*

interface GetUserInvoicesService {
    suspend fun get(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: UUID,
    ): InvoiceListModelLegacy
}