package services.api.fakes.company

import models.company.CompanyDetailsModel
import models.fixtures.companyDetailsFixture
import services.api.services.company.GetCompanyDetailsService
import java.util.*

class FakeGetCompanyDetailsService : GetCompanyDetailsService {

    var response: CompanyDetailsModel? = companyDetailsFixture

    override suspend fun get(companyId: UUID): CompanyDetailsModel? {
        return response
    }
}