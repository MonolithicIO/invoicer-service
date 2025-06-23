package services.impl.company

import models.company.CompanyDetailsModel
import repository.UserCompanyRepository
import services.api.services.company.GetCompanyDetailsService

internal class GetCompanyDetailsService(
    private val companyRepository: UserCompanyRepository
) : GetCompanyDetailsService {

    override suspend fun get(companyId: String): CompanyDetailsModel {
        TODO("Not yet implemented")
    }
}