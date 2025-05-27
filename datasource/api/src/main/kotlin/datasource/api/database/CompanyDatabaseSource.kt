package datasource.api.database

import datasource.api.model.company.CreateCompanyData

interface CompanyDatabaseSource {
    suspend fun createCompany(
        data: CreateCompanyData
    ): String
}