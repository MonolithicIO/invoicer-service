package services.impl.invoice

import kotlinx.coroutines.test.runTest
import models.fixtures.invoiceModelFixture
import models.fixtures.userModelFixture
import repository.fakes.FakeInvoiceRepository
import services.api.fakes.user.FakeGetUserByIdService
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError
import java.util.*
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
        service = GetUserInvoiceByIdServiceImpl(
            repository = repository,
            getUserByIdUseCase = getUserByIdService
        )
    }

    @Test
    fun `should successfully return invoice`() = runTest {
        val invoice = invoiceModelFixture.copy(
            user = userModelFixture
        )

        repository.getInvoiceByIdResponse = { invoice }
        getUserByIdService.response = { userModelFixture }

        val result = service.get(
            invoiceId = invoiceModelFixture.id,
            userId = userModelFixture.id
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
                userId = userModelFixture.id
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

            val invoice = invoiceModelFixture.copy(
                user = userModelFixture.copy(
                    id = UUID.fromString("7956749e-9d8b-4ab7-abd1-29f0b7ecb9b8")
                )
            )

            repository.getInvoiceByIdResponse = { invoice }
            getUserByIdService.response = { userModelFixture }

            service.get(
                invoiceId = invoiceModelFixture.id,
                userId = userModelFixture.id
            )
        }

        assertEquals(
            expected = HttpCode.Forbidden,
            actual = error.statusCode
        )
    }
}