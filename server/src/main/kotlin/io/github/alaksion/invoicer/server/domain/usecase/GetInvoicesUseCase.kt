package io.github.alaksion.invoicer.server.domain.usecase

import io.github.alaksion.invoicer.server.domain.model.getinvoices.GetInvoicesFilterModel
import io.github.alaksion.invoicer.server.domain.model.getinvoices.InvoiceListItemModel
import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository

internal interface GetInvoicesUseCase {
    suspend fun get(filters: GetInvoicesFilterModel, page: Long, limit: Int): List<InvoiceListItemModel>
}

internal class GetInvoicesUseCaseImpl(
    private val repository: InvoiceRepository
) : GetInvoicesUseCase {

    override suspend fun get(filters: GetInvoicesFilterModel, page: Long, limit: Int): List<InvoiceListItemModel> {
        // TODO -> Validate filters
        return repository.getInvoices(filters = filters, page = page, limit = limit)
    }
}