package services.api.services.company

import models.company.CompanyDetailsModel

interface GetCompanyDetailsService {
    suspend fun get(
        companyId: String,
    ): CompanyDetailsModel
}