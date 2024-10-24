package services.api.services.invoice

import models.getinvoices.GetInvoicesFilterModel
import models.getinvoices.InvoiceListItemModel

interface GetUserInvoicesService {
    suspend fun get(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: String,
    ): List<InvoiceListItemModel>
}