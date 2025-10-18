package io.github.monolithic.invoicer.services.company

import io.github.monolithic.invoicer.foundation.exceptions.http.HttpError
import io.github.monolithic.invoicer.models.company.UpdateCompanyAddressModel
import io.github.monolithic.invoicer.models.fixtures.companyDetailsFixture
import io.github.monolithic.invoicer.models.fixtures.userModelFixture
import io.github.monolithic.invoicer.repository.CompanyAddressRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest

class UpdateCompanyAddressServiceImplTest {
    private val getUserByIdService = mockk<GetUserByIdService>()
    private val getCompanyByIdService = mockk<GetCompanyDetailsService>()
    private val companyAddressRepository = mockk<CompanyAddressRepository>()
    private lateinit var service: UpdateCompanyAddressServiceImpl

    @BeforeTest
    fun setup() {
        service = UpdateCompanyAddressServiceImpl(
            getUserByIdService,
            getCompanyByIdService,
            companyAddressRepository
        )
    }

    @Test
    fun `should update company address successfully`() = runTest {
        // Arrange
        val model = UpdateCompanyAddressModel(
            companyId = companyDetailsFixture.id,
            addressLine = "123 Main St",
            addressLine2 = null,
            city = "Test City",
            state = "TS",
            postalCode = "12345"
        )

        coEvery { getUserByIdService.get(any()) } returns userModelFixture
        coEvery { getCompanyByIdService.get(any()) } returns companyDetailsFixture.copy(user = userModelFixture)
        coEvery { companyAddressRepository.updateAddress(model) } just Runs

        // Act
        service.updateCompanyAddress(model, companyDetailsFixture.id)

        // Assert
        coVerify(exactly = 1) { companyAddressRepository.updateAddress(model) }
    }

    @Test
    fun `should throw ForbiddenException when user is not company owner`() = runTest {

        val model = UpdateCompanyAddressModel(
            companyId = companyDetailsFixture.id,
            addressLine = "123 Main St",
            city = "Test City",
            state = "TS",
            postalCode = "12345",
            addressLine2 = null
        )

        coEvery { getUserByIdService.get(any()) } returns userModelFixture
        coEvery { getCompanyByIdService.get(any()) } returns companyDetailsFixture.copy(
            user = userModelFixture.copy(id = UUID.randomUUID())
        )

        assertFailsWith<HttpError> {
            service.updateCompanyAddress(model, userModelFixture.id)
        }
    }

    @Test
    fun `should throw NotFoundException when company not found`() = runTest {

        val model = UpdateCompanyAddressModel(
            companyId = companyDetailsFixture.id,
            addressLine = "123 Main St",
            city = "Test City",
            state = "TS",
            postalCode = "12345",
            addressLine2 = null
        )

        coEvery { getUserByIdService.get(any()) } returns userModelFixture
        coEvery { getCompanyByIdService.get(any()) } returns null

        assertFailsWith<HttpError> {
            service.updateCompanyAddress(model, userModelFixture.id)
        }
    }

    @Test
    fun `should throw BadRequestException when address line is blank`() = runTest {
        coEvery { getUserByIdService.get(any()) } returns userModelFixture

        val model = UpdateCompanyAddressModel(
            companyId = companyDetailsFixture.id,
            addressLine = "",
            city = "Test City",
            state = "TS",
            postalCode = "12345",
            addressLine2 = null
        )

        assertFailsWith<HttpError> {
            service.updateCompanyAddress(model, userModelFixture.id)
        }
    }

    @Test
    fun `should throw BadRequestException when optional address line 2 is blank`() = runTest {
        coEvery { getUserByIdService.get(any()) } returns userModelFixture

        val model = UpdateCompanyAddressModel(
            companyId = companyDetailsFixture.id,
            addressLine = "123 Main St",
            city = "Test City",
            state = "TS",
            postalCode = "12345",
            addressLine2 = " "
        )

        assertFailsWith<HttpError> {
            service.updateCompanyAddress(model, userModelFixture.id)
        }
    }
}
