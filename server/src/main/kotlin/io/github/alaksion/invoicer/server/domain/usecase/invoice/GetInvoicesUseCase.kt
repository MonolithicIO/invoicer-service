package io.github.alaksion.invoicer.server.domain.usecase.invoice

import io.github.alaksion.invoicer.server.domain.model.getinvoices.GetInvoicesFilterModel
import io.github.alaksion.invoicer.server.domain.model.getinvoices.InvoiceListItemModel
import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import io.ktor.http.*
import kotlinx.datetime.LocalDate
import utils.exceptions.httpError

internal interface GetInvoicesUseCase {
    suspend fun get(filters: GetInvoicesFilterModel, page: Long, limit: Int): List<InvoiceListItemModel>
}

internal class GetInvoicesUseCaseImpl(
    private val repository: InvoiceRepository
) : GetInvoicesUseCase {

    override suspend fun get(filters: GetInvoicesFilterModel, page: Long, limit: Int): List<InvoiceListItemModel> {
        validateDateFilter(
            min = filters.minIssueDate,
            max = filters.maxIssueDate
        )

        validateDateFilter(
            min = filters.minDueDate,
            max = filters.maxDueDate
        )
        return repository.getInvoices(filters = filters, page = page, limit = limit)
    }

    private fun validateDateFilter(
        min: LocalDate?,
        max: LocalDate?
    ) {
        if ((min == null && max != null) || (min != null && max == null)) {
            httpError(
                message = "Date filter must contain min and max values.",
                code = HttpStatusCode.BadRequest
            )
        }
    }
}