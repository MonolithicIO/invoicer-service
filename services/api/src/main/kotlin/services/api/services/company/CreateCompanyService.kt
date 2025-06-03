package services.api.services.company

import models.company.CreateCompanyModel
import java.util.*

interface CreateCompanyService {
    suspend fun createCompany(
        data: CreateCompanyModel,
        userId: UUID,
    ): String
}