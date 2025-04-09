package services.api.services.invoice

import models.getinvoices.GetInvoicesFilterModel
import models.getinvoices.InvoiceListModel
import java.util.*

interface GetUserInvoicesService {
    suspend fun get(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: UUID,
    ): InvoiceListModel
}