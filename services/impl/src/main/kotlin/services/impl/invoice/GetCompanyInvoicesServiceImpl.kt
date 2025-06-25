package services.impl.invoice

import kotlinx.datetime.Instant
import models.invoice.GetInvoicesFilterModel
import models.invoice.InvoiceListModel
import repository.InvoiceRepository
import repository.UserCompanyRepository
import services.api.services.invoice.GetCompanyInvoicesService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.HttpCode
import utils.exceptions.http.forbiddenError
import utils.exceptions.http.httpError
import utils.exceptions.http.notFoundError
import java.util.*

internal class GetCompanyInvoicesServiceImpl(
    private val repository: InvoiceRepository,
    private val companyRepository: UserCompanyRepository,
    private val getUserByIdService: GetUserByIdService
) : GetCompanyInvoicesService {

    override suspend fun get(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: UUID,
        companyId: UUID
    ): InvoiceListModel {
        validateDateFilter(
            min = filters.minIssueDate,
            max = filters.maxIssueDate
        )

        validateDateFilter(
            min = filters.minDueDate,
            max = filters.maxDueDate
        )

        val user = getUserByIdService.get(userId)

        val company = companyRepository.getCompanyById(companyId) ?: notFoundError("Company not found.")

        if (company.userId != user.id) forbiddenError()

        return repository.getAll(
            filters = filters,
            page = page,
            limit = limit,
            companyId = companyId
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