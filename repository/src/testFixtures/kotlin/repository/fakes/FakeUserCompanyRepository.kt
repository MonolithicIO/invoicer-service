package repository.fakes

import kotlinx.datetime.Instant
import models.company.*
import models.fixtures.userModelFixture
import models.paymentaccount.PaymentAccountModel
import models.paymentaccount.PaymentAccountTypeModel
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
    var detailsResponse: () -> CompanyDetailsModel? = {
        CompanyDetailsModel(
            name = "Test Company",
            document = "123456789",
            createdAt = Instant.parse("2023-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2023-01-02T00:00:00Z"),
            isDeleted = false,
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
            address = CompanyAddressModel(
                addressLine1 = "123 Test St",
                addressLine2 = null,
                city = "Test City",
                state = "Test State",
                postalCode = "12345",
                countryCode = "US"
            ),
            paymentAccount = PaymentAccountModel(
                swift = "ABCDEF12",
                iban = "DE89370400440532013000",
                bankName = "Bank of Beneficiary",
                bankAddress = "789 Bank St",
                type = PaymentAccountTypeModel.Primary,
                isDeleted = false,
                createdAt = Instant.parse("2023-01-01T00:00:00Z"),
                updatedAt = Instant.parse("2023-01-02T00:00:00Z"),
                id = UUID.fromString("123e4567-e89b-12d3-a456-426614174003"),
                companyId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
            ),
            intermediaryAccount = null,
            user = userModelFixture
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

    override suspend fun getCompanyDetails(companyId: UUID): CompanyDetailsModel? {
        return detailsResponse()
    }
}