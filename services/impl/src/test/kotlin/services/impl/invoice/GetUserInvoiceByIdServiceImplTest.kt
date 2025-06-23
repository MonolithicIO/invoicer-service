package services.impl.invoice

import kotlinx.coroutines.test.runTest
import models.fixtures.invoiceModelFixture
import models.fixtures.userModelFixture
import repository.fakes.FakeInvoiceRepository
import services.api.fakes.user.FakeGetUserByIdService
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetUserInvoiceByIdServiceImplTest {

    private lateinit var service: GetUserInvoiceByIdServiceImpl
    private lateinit var repository: FakeInvoiceRepository
    private lateinit var getUserByIdService: FakeGetUserByIdService


    @BeforeTest
    fun setUp() {
        repository = FakeInvoiceRepository()
        getUserByIdService = FakeGetUserByIdService()
        service = GetUserInvoiceByIdServiceImpl(repository = repository)
    }

    @Test
    fun `should successfully return invoice`() = runTest {
        val invoice = invoiceModelFixture

        repository.getInvoiceByIdResponse = { invoice }
        getUserByIdService.response = { userModelFixture }

        val result = service.get(
            invoiceId = invoiceModelFixture.id,
        )

        assertEquals(
            expected = invoice,
            actual = result
        )
    }

    @Test
    fun `should throw error if invoice does not exist`() = runTest {
        val error = assertFailsWith<HttpError> {
            repository.getInvoiceByIdResponse = { null }
            service.get(
                invoiceId = invoiceModelFixture.id,
            )
        }

        assertEquals(
            expected = HttpCode.NotFound,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error if user is not invoice owner`() = runTest {
        val error = assertFailsWith<HttpError> {

            val invoice = invoiceModelFixture

            repository.getInvoiceByIdResponse = { invoice }
            getUserByIdService.response = { userModelFixture }

            service.get(
                invoiceId = invoiceModelFixture.id,
            )
        }

        assertEquals(
            expected = HttpCode.Forbidden,
            actual = error.statusCode
        )
    }
}