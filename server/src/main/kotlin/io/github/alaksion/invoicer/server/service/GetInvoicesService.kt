package io.github.alaksion.invoicer.server.service

import io.github.alaksion.invoicer.server.entities.InvoiceEntity
import io.github.alaksion.invoicer.server.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.viewmodel.GetInvoicesFilterViewModel

internal interface GetInvoicesService {
    suspend fun get(filters: GetInvoicesFilterViewModel, page: Long, limit: Int): List<InvoiceEntity>
}

internal class GetInvoicesServiceImpl(
    private val repository: InvoiceRepository
) : GetInvoicesService {

    override suspend fun get(filters: GetInvoicesFilterViewModel, page: Long, limit: Int): List<InvoiceEntity> {
        return repository.getInvoices(filterModel = filters, page = page, limit = limit)
    }

}