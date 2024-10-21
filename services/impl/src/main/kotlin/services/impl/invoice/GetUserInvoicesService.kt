package services.impl.invoice

import kotlinx.datetime.LocalDate
import models.getinvoices.GetInvoicesFilterModel
import models.getinvoices.InvoiceListItemModel
import repository.api.repository.InvoiceRepository
import utils.exceptions.HttpCode
import utils.exceptions.httpError

interface GetUserInvoicesService {
    suspend fun get(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: String,
    ): List<InvoiceListItemModel>
}

internal class GetUserInvoicesServiceImpl(
    private val repository: InvoiceRepository
) : GetUserInvoicesService {

    override suspend fun get(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: String,
    ): List<InvoiceListItemModel> {
        validateDateFilter(
            min = filters.minIssueDate,
            max = filters.maxIssueDate
        )

        validateDateFilter(
            min = filters.minDueDate,
            max = filters.maxDueDate
        )
        return repository.getInvoices(
            filters = filters,
            page = page,
            limit = limit,
            userId = userId
        )
    }

    private fun validateDateFilter(
        min: LocalDate?,
        max: LocalDate?
    ) {
        if ((min == null && max != null) || (min != null && max == null)) {
            httpError(
                message = "Date filter must contain min and max values.",
                code = HttpCode.BadRequest
            )
        }
    }
}