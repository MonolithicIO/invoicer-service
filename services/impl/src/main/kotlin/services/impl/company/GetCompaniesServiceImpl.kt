package services.impl.company

import models.company.CompanyList
import repository.UserCompanyRepository
import services.api.services.company.GetCompaniesService
import services.api.services.user.GetUserByIdService
import java.util.*

internal class GetCompaniesServiceImpl(
    private val repository: UserCompanyRepository,
    private val getUserByIdService: GetUserByIdService
) : GetCompaniesService {

    override suspend fun get(
        userId: UUID,
        page: Int,
        limit: Int
    ): CompanyList {
        getUserByIdService.get(userId)

        return repository.getCompaniesByUserId(
            userId = userId,
            page = page,
            limit = limit
        )
    }
}
