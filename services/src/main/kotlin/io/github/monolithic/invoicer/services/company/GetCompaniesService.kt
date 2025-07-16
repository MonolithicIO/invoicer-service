package io.github.monolithic.invoicer.services.company

import io.github.monolithic.invoicer.models.company.CompanyList
import io.github.monolithic.invoicer.repository.UserCompanyRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import java.util.*

interface GetCompaniesService {
    suspend fun get(
        userId: UUID,
        page: Int,
        limit: Int
    ): CompanyList
}

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
