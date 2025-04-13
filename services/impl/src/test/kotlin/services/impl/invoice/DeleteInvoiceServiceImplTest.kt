package services.impl.invoice

import kotlinx.coroutines.test.runTest
import models.fixtures.invoiceModelFixture
import models.fixtures.userModelFixture
import repository.api.fakes.FakeInvoiceRepository
import services.api.fakes.invoice.FakeGetUserInvoiceByIdService
import services.api.fakes.user.FakeGetUserByIdService
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class DeleteInvoiceServiceImplTest {

    private lateinit var service: DeleteInvoiceServiceImpl
    private lateinit var getUserService: FakeGetUserByIdService
    private lateinit var getInvoiceService: FakeGetUserInvoiceByIdService
    private lateinit var repository: FakeInvoiceRepository

    @BeforeTest
    fun setUp() {
        getUserService = FakeGetUserByIdService()
        repository = FakeInvoiceRepository()
        getInvoiceService = FakeGetUserInvoiceByIdService()

        service = DeleteInvoiceServiceImpl(
            getUserInvoiceByIdService = getInvoiceService,
            getUserByIdUseCase = getUserService,
            repository = repository
        )
    }

    @Test
    fun `should delete invoice successfully`() = runTest {
        getInvoiceService.response = { invoiceModelFixture }
        getUserService.response = { userModelFixture }

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
    fun `should throw error if user is not owner of invoice`() = runTest {
        val error = assertFailsWith<HttpError> {
            getInvoiceService.response = { invoiceModelFixture }
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
            expected = HttpCode.Forbidden,
            actual = error.statusCode
        )
    }
}