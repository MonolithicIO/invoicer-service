package io.github.alaksion.invoicer.services.company

import models.company.CompanyDetailsModel
import java.util.*
import repository.UserCompanyRepository

interface GetCompanyDetailsService {
    suspend fun get(
        companyId: UUID,
    ): CompanyDetailsModel?
}

internal class GetCompanyDetailsServiceImpl(
    private val companyRepository: UserCompanyRepository
) : GetCompanyDetailsService {

    override suspend fun get(companyId: UUID): CompanyDetailsModel? {
        return companyRepository.getCompanyDetails(companyId)
    }
}
