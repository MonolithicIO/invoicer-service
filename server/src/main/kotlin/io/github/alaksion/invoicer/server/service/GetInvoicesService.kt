package io.github.alaksion.invoicer.server.service

import io.github.alaksion.invoicer.server.entities.InvoiceEntity
import io.github.alaksion.invoicer.server.models.GetInvoicesFilterModel
import io.github.alaksion.invoicer.server.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.viewmodel.getinvoices.GetInvoicesFilterViewModel

internal interface GetInvoicesService {
    suspend fun get(filters: GetInvoicesFilterViewModel, page: Long, limit: Int): List<InvoiceEntity>
}

internal class GetInvoicesServiceImpl(
    private val repository: InvoiceRepository
) : GetInvoicesService {

    override suspend fun get(filters: GetInvoicesFilterViewModel, page: Long, limit: Int): List<InvoiceEntity> {
        val filters = mapFilters(filters)
        return repository.getInvoices(filterModel = filters, page = page, limit = limit)
    }

    private fun mapFilters(viewModel: GetInvoicesFilterViewModel): GetInvoicesFilterModel {
        return GetInvoicesFilterModel(null, null, null, null, null, null)
    }

}