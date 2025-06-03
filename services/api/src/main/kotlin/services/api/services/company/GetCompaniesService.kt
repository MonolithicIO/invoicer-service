package services.api.services.company

import models.company.CompanyList
import java.util.*

interface GetCompaniesService {
    suspend fun get(
        userId: UUID,
        page: Int,
        limit: Int
    ): CompanyList
}