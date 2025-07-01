package services.api.services.company

import models.company.CompanyDetailsModel
import java.util.*

interface GetUserCompanyDetailsService {
    suspend fun get(
        userId: UUID,
        companyId: UUID
    ): CompanyDetailsModel
}