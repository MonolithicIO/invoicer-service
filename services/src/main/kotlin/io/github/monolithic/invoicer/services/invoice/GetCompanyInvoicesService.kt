package io.github.monolithic.invoicer.services.invoice

import io.github.monolithic.invoicer.models.invoice.GetInvoicesFilterModel
import io.github.monolithic.invoicer.models.invoice.InvoiceListModel
import java.util.*
import kotlinx.datetime.Instant
import io.github.monolithic.invoicer.repository.InvoiceRepository
import io.github.monolithic.invoicer.repository.UserCompanyRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import io.github.monolithic.invoicer.foundation.exceptions.http.HttpCode
import io.github.monolithic.invoicer.foundation.exceptions.http.forbiddenError
import io.github.monolithic.invoicer.foundation.exceptions.http.httpError
import io.github.monolithic.invoicer.foundation.exceptions.http.notFoundError

interface GetCompanyInvoicesService {
    suspend fun get(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: UUID,
        companyId: UUID,
    ): InvoiceListModel
}

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
        @Suppress("ComplexCondition")
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
