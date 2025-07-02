package services.impl.company

import models.company.CompanyDetailsModel
import repository.UserCompanyRepository
import services.api.services.company.GetUserCompanyDetailsService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.forbiddenError
import utils.exceptions.http.notFoundError
import java.util.*

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
