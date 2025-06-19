package repository.fakes

import models.company.CompanyList
import models.company.CompanyModel
import models.company.CreateCompanyModel
import repository.UserCompanyRepository
import java.util.*

class FakeUserCompanyRepository : UserCompanyRepository {

    var getCompanyByIdResponse: () -> CompanyModel? = { null }
    var getCompaniesByUserId: () -> CompanyList = {
        CompanyList(
            items = listOf(),
            totalCount = 0,
            nextPage = null
        )
    }

    override suspend fun createCompany(data: CreateCompanyModel, userId: UUID): String = "1234"

    override suspend fun getCompaniesByUserId(
        userId: UUID,
        page: Int,
        limit: Int
    ): CompanyList {
        return getCompaniesByUserId()
    }

    override suspend fun getCompanyById(companyId: UUID): CompanyModel? {
        return getCompanyByIdResponse()
    }
}