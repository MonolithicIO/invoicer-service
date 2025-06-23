package services.impl.invoice

import kotlinx.coroutines.test.runTest
import models.fixtures.invoiceModelFixture
import models.fixtures.userModelFixture
import repository.fakes.FakeInvoiceRepository
import services.api.fakes.user.FakeGetUserByIdService
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

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
    fun `should return null if invoice does not exist`() = runTest {

        repository.getInvoiceByIdResponse = { null }
        assertNull(
            service.get(
                invoiceId = invoiceModelFixture.id,
            )
        )
    }
}