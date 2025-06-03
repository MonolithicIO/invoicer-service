package datasource.api.database

import datasource.api.model.company.CreateCompanyData
import models.company.CompanyList
import java.util.*

interface CompanyDatabaseSource {
    suspend fun createCompany(
        data: CreateCompanyData
    ): String

    suspend fun getUserCompanies(
        userId: UUID,
        page: Int,
        limit: Int
    ): CompanyList
}