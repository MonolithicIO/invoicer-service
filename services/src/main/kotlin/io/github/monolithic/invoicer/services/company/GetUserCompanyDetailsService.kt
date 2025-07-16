package io.github.monolithic.invoicer.services.company

import java.util.*
import io.github.monolithic.invoicer.models.company.CompanyDetailsModel
import io.github.monolithic.invoicer.repository.UserCompanyRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import io.github.monolithic.invoicer.foundation.exceptions.http.forbiddenError
import io.github.monolithic.invoicer.foundation.exceptions.http.notFoundError

interface GetUserCompanyDetailsService {

    /**
     * Searches for a company by user ID and company ID.
     * @exception [io.github.monolithic.invoicer.foundation.exceptions.http.HttpError] if user is not found.
     * @exception [io.github.monolithic.invoicer.foundation.exceptions.http.HttpError] if company is not found.
     * @exception [io.github.monolithic.invoicer.foundation.exceptions.http.HttpError] if user has no access to
     * the company.
     * */
    suspend fun get(
        userId: UUID,
        companyId: UUID
    ): CompanyDetailsModel
}

internal class GetUserCompanyDetailsServiceImpl(
    private val companyRepository: UserCompanyRepository,
    private val getUserByIdService: GetUserByIdService

) : GetUserCompanyDetailsService {
    override suspend fun get(userId: UUID, companyId: UUID): CompanyDetailsModel {
        val user = getUserByIdService.get(userId)
        val company = companyRepository.getCompanyDetails(companyId) ?: notFoundError("Company not found")

        if (user.id != company.user.id) forbiddenError()

        return company
    }
}
