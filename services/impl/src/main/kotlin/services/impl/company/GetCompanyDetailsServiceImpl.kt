package services.impl.company

import models.company.CompanyDetailsModel
import repository.UserCompanyRepository
import services.api.services.company.GetCompanyDetailsService
import java.util.UUID

internal class GetCompanyDetailsServiceImpl(
    private val companyRepository: UserCompanyRepository
) : GetCompanyDetailsService {

    override suspend fun get(companyId: UUID): CompanyDetailsModel? {
        return companyRepository.getCompanyDetails(companyId)
    }
}
