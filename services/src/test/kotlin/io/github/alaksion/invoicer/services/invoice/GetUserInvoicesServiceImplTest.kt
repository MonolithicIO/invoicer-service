package io.github.alaksion.invoicer.services.invoice

import io.github.alaksion.invoicer.services.fakes.user.FakeGetUserByIdService
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import models.company.CompanyModel
import models.fixtures.invoiceListFixture
import models.fixtures.userModelFixture
import models.invoice.GetInvoicesFilterModel
import repository.fakes.FakeInvoiceRepository
import repository.fakes.FakeUserCompanyRepository
import services.api.services.invoice.GetCompanyInvoicesServiceImpl
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError

class GetUserInvoicesServiceImplTest {

    private lateinit var service: GetCompanyInvoicesServiceImpl
    private lateinit var repository: FakeInvoiceRepository
    private lateinit var companyRepository: FakeUserCompanyRepository
    private lateinit var getuserByIdService: FakeGetUserByIdService

    @BeforeTest
    fun setUp() {
        repository = FakeInvoiceRepository()
        companyRepository = FakeUserCompanyRepository()
        getuserByIdService = FakeGetUserByIdService()
        service = GetCompanyInvoicesServiceImpl(
            repository = repository,
            companyRepository = companyRepository,
            getUserByIdService = getuserByIdService,
        )
    }

    @Test
    fun `should return invoices`() = runTest {
        repository.getInvoicesResponse = { invoiceListFixture }
        companyRepository.getCompanyByIdResponse = { company }
        getuserByIdService.response = { userModelFixture }

        val filters = GetInvoicesFilterModel(
            minIssueDate = Instant.parse("2000-06-19T00:00:00Z"),
            maxIssueDate = Instant.parse("2000-06-20T00:00:00Z"),
            minDueDate = null,
            maxDueDate = null,
        )

        val page = 1L
        val limit = 10

        val result = service.get(
            filters, page, limit, userModelFixture.id, companyId
        )

        assertEquals(
            expected = invoiceListFixture,
            actual = result
        )
    }

    @Test
    fun `should throw error if min date filter is present but max date is not`() = runTest {
        val error = assertFailsWith<HttpError> {
            repository.getInvoicesResponse = { invoiceListFixture }

            val filters = GetInvoicesFilterModel(
                minIssueDate = Instant.parse("2000-06-19T00:00:00Z"),
                maxIssueDate = null,
                minDueDate = null,
                maxDueDate = null,
            )

            val page = 1L
            val limit = 10

            service.get(filters, page, limit, userModelFixture.id, companyId)
        }
        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error if max date filter is present but min date is not`() = runTest {
        val error = assertFailsWith<HttpError> {
            repository.getInvoicesResponse = { invoiceListFixture }

            val filters = GetInvoicesFilterModel(
                minIssueDate = null,
                maxIssueDate = Instant.parse("2000-06-19T00:00:00Z"),
                minDueDate = null,
                maxDueDate = null,
            )

            val page = 1L
            val limit = 10

            service.get(filters, page, limit, userModelFixture.id, companyId)
        }
        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error if min filter is greater than max filter`() = runTest {
        val error = assertFailsWith<HttpError> {
            repository.getInvoicesResponse = { invoiceListFixture }

            val filters = GetInvoicesFilterModel(
                minIssueDate = Instant.parse("2000-06-20T00:00:00Z"),
                maxIssueDate = Instant.parse("2000-06-19T00:00:00Z"),
                minDueDate = null,
                maxDueDate = null,
            )

            val page = 1L
            val limit = 10

            service.get(filters, page, limit, userModelFixture.id, companyId)
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

    @Test
    fun `should throw error if company does not exist`() = runTest {
        repository.getInvoicesResponse = { invoiceListFixture }
        companyRepository.getCompanyByIdResponse = { null }
        getuserByIdService.response = { userModelFixture }

        val filters = GetInvoicesFilterModel(
            minIssueDate = null,
            maxIssueDate = null,
            minDueDate = null,
            maxDueDate = null,
        )

        val error = assertFailsWith<HttpError> {
            service.get(
                filters, 0, 10, userModelFixture.id, companyId
            )
        }

        assertEquals(
            expected = HttpCode.NotFound,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error if company does not belong to user`() = runTest {
        repository.getInvoicesResponse = { invoiceListFixture }
        companyRepository.getCompanyByIdResponse = {
            company.copy(
                userId = UUID.randomUUID()
            )
        }
        getuserByIdService.response = { userModelFixture }

        val filters = GetInvoicesFilterModel(
            minIssueDate = null,
            maxIssueDate = null,
            minDueDate = null,
            maxDueDate = null,
        )

        val error = assertFailsWith<HttpError> {
            service.get(
                filters, 0, 10, userModelFixture.id, companyId
            )
        }

        assertEquals(
            expected = HttpCode.Forbidden,
            actual = error.statusCode
        )
    }

    companion object {
        val companyId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001")

        val company = CompanyModel(
            name = "some company",
            document = "awdaw",
            createdAt = Instant.parse("2023-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2023-01-01T00:00:00Z"),
            isDeleted = false,
            userId = userModelFixture.id,
            id = companyId
        )
    }
}
