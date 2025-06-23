package services.api.services.invoice

import models.invoice.GetInvoicesFilterModel
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