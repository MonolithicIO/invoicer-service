package services.api.services.invoice

import models.getinvoices.GetInvoicesFilterModel
import models.getinvoices.InvoiceListModelLegacy
import models.invoice.InvoiceListModel
import java.util.*

interface GetUserInvoicesService {
    suspend fun get(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: UUID,
    ): InvoiceListModel
}