package services.impl.invoice

import kotlinx.datetime.Instant
import models.invoice.GetInvoicesFilterModel
import models.invoice.InvoiceListModel
import repository.InvoiceRepository
import services.api.services.invoice.GetCompanyInvoicesService
import utils.exceptions.http.HttpCode
import utils.exceptions.http.httpError
import java.util.*

internal class GetCompanyInvoicesServiceImpl(
    private val repository: InvoiceRepository
) : GetCompanyInvoicesService {

    override suspend fun get(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: UUID,
    ): InvoiceListModel {
        validateDateFilter(
            min = filters.minIssueDate,
            max = filters.maxIssueDate
        )

        validateDateFilter(
            min = filters.minDueDate,
            max = filters.maxDueDate
        )
        return repository.getAll(
            filters = filters,
            page = page,
            limit = limit,
            companyId = userId
        )
    }

    private fun validateDateFilter(
        min: Instant?,
        max: Instant?
    ) {
        if ((min == null && max != null) || (min != null && max == null)) {
            httpError(
                message = "Date filter must contain min and max values.",
                code = HttpCode.BadRequest
            )
        }

        if (min != null && max != null && (min > max)) {
            httpError(
                message = "Min date filter must be less than max date filter.",
                code = HttpCode.BadRequest
            )
        }
    }
}