package io.github.alaksion.invoicer.server.domain.usecase

import io.github.alaksion.invoicer.server.data.entities.InvoiceEntity
import io.github.alaksion.invoicer.server.domain.model.GetInvoicesFilterModel
import io.github.alaksion.invoicer.server.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.GetInvoicesFilterViewModel

internal interface GetInvoicesUseCase {
    suspend fun get(filters: GetInvoicesFilterViewModel, page: Long, limit: Int): List<InvoiceEntity>
}

internal class GetInvoicesUseCaseImpl(
    private val repository: InvoiceRepository
) : GetInvoicesUseCase {

    override suspend fun get(filters: GetInvoicesFilterViewModel, page: Long, limit: Int): List<InvoiceEntity> {
        val filters = mapFilters(filters)
        return repository.getInvoices(filterModel = filters, page = page, limit = limit)
    }

    private fun mapFilters(viewModel: GetInvoicesFilterViewModel): GetInvoicesFilterModel {
        return GetInvoicesFilterModel(null, null, null, null, null, null)
    }

}