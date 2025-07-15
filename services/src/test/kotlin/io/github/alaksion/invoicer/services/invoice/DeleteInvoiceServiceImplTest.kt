package io.github.alaksion.invoicer.services.invoice

import io.github.alaksion.invoicer.services.fakes.company.FakeGetCompanyDetailsService
import io.github.alaksion.invoicer.services.fakes.user.FakeGetUserByIdService
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import models.fixtures.companyDetailsFixture
import models.fixtures.invoiceModelFixture
import models.fixtures.userModelFixture
import models.invoice.InvoiceCompanyModel
import repository.fakes.FakeInvoiceRepository
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError


class DeleteInvoiceServiceImplTest {

    private lateinit var service: DeleteInvoiceServiceImpl
    private lateinit var getUserService: FakeGetUserByIdService
    private lateinit var repository: FakeInvoiceRepository
    private lateinit var getCompanyDetailsService: FakeGetCompanyDetailsService

    @BeforeTest
    fun setUp() {
        getUserService = FakeGetUserByIdService()
        repository = FakeInvoiceRepository()
        getCompanyDetailsService = FakeGetCompanyDetailsService()

        service = DeleteInvoiceServiceImpl(
            getUserByIdUseCase = getUserService,
            repository = repository,
            getCompanyByIdService = getCompanyDetailsService
        )
    }

    @Test
    fun `should delete invoice successfully`() = runTest {

        repository.getInvoiceByIdResponse = { invoice }
        getUserService.response = { user }
        getCompanyDetailsService.response = company

        service.delete(
            invoiceId = invoiceModelFixture.id,
            userId = userModelFixture.id
        )

        assertEquals(
            expected = 1,
            actual = repository.deleteCallStack.size
        )

        assertEquals(
            expected = invoiceModelFixture.id,
            actual = repository.deleteCallStack[0]
        )
    }

    @Test
    fun `should throw error if invoice not exists`() = runTest {
        val error = assertFailsWith<HttpError> {
            repository.getInvoiceByIdResponse = { null }
            getUserService.response = {
                userModelFixture.copy(
                    id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                )
            }

            service.delete(
                invoiceId = invoiceModelFixture.id,
                userId = userModelFixture.id
            )
        }
        assertEquals(
            expected = HttpCode.NotFound,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error if invoice company not exists`() = runTest {
        val error = assertFailsWith<HttpError> {
            repository.getInvoiceByIdResponse = { invoiceModelFixture }
            getCompanyDetailsService.response = null

            getUserService.response = {
                userModelFixture.copy(
                    id = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")
                )
            }

            service.delete(
                invoiceId = invoiceModelFixture.id,
                userId = userModelFixture.id
            )
        }
        assertEquals(
            expected = HttpCode.NotFound,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error if invoice company does not belong to user`() = runTest {
        val error = assertFailsWith<HttpError> {
            repository.getInvoiceByIdResponse = { invoice }
            getCompanyDetailsService.response = company.copy(
                user = userModelFixture.copy(
                    id = UUID.fromString("e143435a-67b7-4daf-9043-8e763877a9c6")
                )
            )
            getUserService.response = { user }

            service.delete(
                invoiceId = invoiceModelFixture.id,
                userId = userModelFixture.id
            )
        }
        assertEquals(
            expected = HttpCode.Forbidden,
            actual = error.statusCode
        )
    }

    companion object {
        val user = userModelFixture
        val company = companyDetailsFixture.copy(user = user)
        val invoice = invoiceModelFixture.copy(
            company = InvoiceCompanyModel(
                id = company.id,
                name = company.name,
                document = company.document,
                addressLine1 = company.address.addressLine1,
                addressLine2 = company.address.addressLine2,
                city = company.address.city,
                zipCode = company.address.postalCode,
                state = company.address.state,
                countryCode = company.address.countryCode,
            )
        )
    }
}
