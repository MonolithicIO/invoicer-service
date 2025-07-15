package io.github.alaksion.invoicer.services.company

import java.util.*
import models.company.CompanyDetailsModel
import repository.UserCompanyRepository
import io.github.alaksion.invoicer.services.user.GetUserByIdService
import utils.exceptions.http.forbiddenError
import utils.exceptions.http.notFoundError

interface GetUserCompanyDetailsService {

    /**
     * Searches for a company by user ID and company ID.
     * @exception [utils.exceptions.http.HttpError] if user is not found.
     * @exception [utils.exceptions.http.HttpError] if company is not found.
     * @exception [utils.exceptions.http.HttpError] if user has no access to the company.
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
