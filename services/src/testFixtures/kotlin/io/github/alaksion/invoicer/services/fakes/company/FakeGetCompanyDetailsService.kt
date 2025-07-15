package io.github.alaksion.invoicer.services.fakes.company

import io.github.alaksion.invoicer.services.company.GetCompanyDetailsService
import java.util.*
import models.company.CompanyDetailsModel
import models.fixtures.companyDetailsFixture

class FakeGetCompanyDetailsService : GetCompanyDetailsService {

    var response: CompanyDetailsModel? = companyDetailsFixture

    override suspend fun get(companyId: UUID): CompanyDetailsModel? {
        return response
    }
}