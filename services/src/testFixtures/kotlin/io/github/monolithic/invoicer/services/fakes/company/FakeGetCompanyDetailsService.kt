package io.github.monolithic.invoicer.services.fakes.company

import io.github.monolithic.invoicer.models.company.CompanyDetailsModel
import io.github.monolithic.invoicer.models.fixtures.companyDetailsFixture
import io.github.monolithic.invoicer.services.company.GetCompanyDetailsService
import java.util.*

class FakeGetCompanyDetailsService : GetCompanyDetailsService {

    var response: CompanyDetailsModel? = companyDetailsFixture

    override suspend fun get(companyId: UUID): CompanyDetailsModel? {
        return response
    }
}