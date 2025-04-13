package services.impl.invoice

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import models.fixtures.invoiceListModelFixture
import models.fixtures.userModelFixture
import models.getinvoices.GetInvoicesFilterModel
import repository.api.fakes.FakeInvoiceRepository
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetUserInvoicesServiceImplTest {

    private lateinit var service: GetUserInvoicesServiceImpl
    private lateinit var repository: FakeInvoiceRepository

    @BeforeTest
    fun setUp() {
        repository = FakeInvoiceRepository()
        service = GetUserInvoicesServiceImpl(repository)
    }

    @Test
    fun `should return invoices`() = runTest {
        repository.getInvoicesResponse = { invoiceListModelFixture }

        val filters = GetInvoicesFilterModel(
            minIssueDate = Instant.parse("2000-06-19T00:00:00Z"),
            maxIssueDate = Instant.parse("2000-06-20T00:00:00Z"),
            minDueDate = null,
            maxDueDate = null,
            senderCompanyName = null,
            recipientCompanyName = null
        )

        val page = 1L
        val limit = 10

        val result = service.get(filters, page, limit, userModelFixture.id)

        assertEquals(
            expected = invoiceListModelFixture,
            actual = result
        )
    }

    @Test
    fun `should return invoices filtered by date`() = runTest {
        repository.getInvoicesResponse = { invoiceListModelFixture }

        val filters = GetInvoicesFilterModel(
            minIssueDate = null,
            maxIssueDate = null,
            minDueDate = null,
            maxDueDate = null,
            senderCompanyName = null,
            recipientCompanyName = null
        )

        val page = 1L
        val limit = 10

        val result = service.get(filters, page, limit, userModelFixture.id)

        assertEquals(
            expected = invoiceListModelFixture,
            actual = result
        )
    }

    @Test
    fun `should throw error if min date filter is present but max date is not`() = runTest {
        val error = assertFailsWith<HttpError> {
            repository.getInvoicesResponse = { invoiceListModelFixture }

            val filters = GetInvoicesFilterModel(
                minIssueDate = Instant.parse("2000-06-19T00:00:00Z"),
                maxIssueDate = null,
                minDueDate = null,
                maxDueDate = null,
                senderCompanyName = null,
                recipientCompanyName = null
            )

            val page = 1L
            val limit = 10

            service.get(filters, page, limit, userModelFixture.id)
        }
        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error if max date filter is present but min date is not`() = runTest {
        val error = assertFailsWith<HttpError> {
            repository.getInvoicesResponse = { invoiceListModelFixture }

            val filters = GetInvoicesFilterModel(
                minIssueDate = null,
                maxIssueDate = Instant.parse("2000-06-19T00:00:00Z"),
                minDueDate = null,
                maxDueDate = null,
                senderCompanyName = null,
                recipientCompanyName = null
            )

            val page = 1L
            val limit = 10

            service.get(filters, page, limit, userModelFixture.id)
        }
        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error if min filter is greater than max filter`() = runTest {
        val error = assertFailsWith<HttpError> {
            repository.getInvoicesResponse = { invoiceListModelFixture }

            val filters = GetInvoicesFilterModel(
                minIssueDate = Instant.parse("2000-06-20T00:00:00Z"),
                maxIssueDate = Instant.parse("2000-06-19T00:00:00Z"),
                minDueDate = null,
                maxDueDate = null,
                senderCompanyName = null,
                recipientCompanyName = null
            )

            val page = 1L
            val limit = 10

            service.get(filters, page, limit, userModelFixture.id)
        }
        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )

        assertEquals(
            expected = "Min date filter must be less than max date filter.",
            actual = error.message
        )
    }
}