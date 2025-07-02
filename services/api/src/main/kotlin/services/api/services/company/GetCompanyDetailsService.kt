package services.api.services.company

import models.company.CompanyDetailsModel
import java.util.*

interface GetCompanyDetailsService {
    suspend fun get(
        companyId: UUID,
    ): CompanyDetailsModel?
}
