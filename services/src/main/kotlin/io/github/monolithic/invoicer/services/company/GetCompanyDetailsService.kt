package io.github.monolithic.invoicer.services.company

import io.github.monolithic.invoicer.models.company.CompanyDetailsModel
import java.util.*
import io.github.monolithic.invoicer.repository.UserCompanyRepository

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
