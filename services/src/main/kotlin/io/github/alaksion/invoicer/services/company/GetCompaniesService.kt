package io.github.alaksion.invoicer.services.company

import models.company.CompanyList
import java.util.*
import repository.UserCompanyRepository
import io.github.alaksion.invoicer.services.user.GetUserByIdService

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
