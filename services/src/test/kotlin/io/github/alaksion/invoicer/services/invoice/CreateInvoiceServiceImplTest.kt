package io.github.alaksion.invoicer.services.invoice

import io.github.alaksion.invoicer.foundation.messaging.MessageTopic
import io.github.alaksion.invoicer.messaging.fakes.FakeMessageProducer
import io.github.alaksion.invoicer.services.fakes.company.FakeGetCompanyDetailsService
import io.github.alaksion.invoicer.services.fakes.customer.FakeCustomerByIdService
import io.github.alaksion.invoicer.services.fakes.user.FakeGetUserByIdService
import io.github.alaksion.invoicer.utils.fakes.FakeClock
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import models.customer.CustomerModel
import models.fixtures.companyDetailsFixture
import models.fixtures.invoiceModelFixture
import models.fixtures.userModelFixture
import models.invoice.CreateInvoiceActivityModel
import models.invoice.CreateInvoiceDTO
import org.junit.Before
import org.junit.Test
import repository.fakes.FakeInvoiceRepository
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError

class CreateInvoiceServiceImplTest {

    private lateinit var service: CreateInvoiceServiceImpl
    private lateinit var invoiceRepository: FakeInvoiceRepository
    private lateinit var clock: FakeClock
    private lateinit var getUserByIdService: FakeGetUserByIdService
    private lateinit var messageProducer: FakeMessageProducer
    private lateinit var getCompanyDetailsService: FakeGetCompanyDetailsService
    private lateinit var customerByIdService: FakeCustomerByIdService

    @Before
    fun setUp() {
        invoiceRepository = FakeInvoiceRepository()
        clock = FakeClock()
        getUserByIdService = FakeGetUserByIdService()
        getCompanyDetailsService = FakeGetCompanyDetailsService()
        messageProducer = FakeMessageProducer()
        customerByIdService = FakeCustomerByIdService()

        service = CreateInvoiceServiceImpl(
            invoiceRepository = invoiceRepository,
            clock = clock,
            getUserByIdService = getUserByIdService,
            messageProducer = messageProducer,
            getCompanyDetailsService = getCompanyDetailsService,
            getCustomerByIdService = customerByIdService,
        )
    }

    @Test
    fun `should create invoice successfully`() = runTest {
        val today = Instant.parse("2000-06-19T00:00:00Z")
        val invoiceResponse = "fed3e1ac-c755-4048-9315-356054c4da11"

        clock.nowResponse = today

        getCompanyDetailsService.response = company
        customerByIdService.response = customer(company.id)

        invoiceRepository.createInvoiceResponse = { invoiceResponse }
        getUserByIdService.response = { userModelFixture }
        invoiceRepository.getInvoiceByExternalIdResponse = { null }

        service.createInvoice(
            BASE_INPUT,
            userId = userModelFixture.id
        )

        assertEquals(
            expected = 1,
            actual = invoiceRepository.createCallStack.size
        )
        assertEquals(
            expected = invoiceResponse,
            actual = invoiceRepository.createCallStack[0]
        )
    }

    @Test
    fun `should send PDF message on create success`() = runTest {
        val today = Instant.parse("2000-06-19T00:00:00Z")
        val invoiceResponse = "fed3e1ac-c755-4048-9315-356054c4da11"

        clock.nowResponse = today

        getCompanyDetailsService.response = company
        customerByIdService.response = customer(company.id)

        invoiceRepository.createInvoiceResponse = { invoiceResponse }
        getUserByIdService.response = { userModelFixture }
        invoiceRepository.getInvoiceByExternalIdResponse = { null }

        val response = service.createInvoice(
            BASE_INPUT,
            userId = userModelFixture.id
        )

        assertEquals(
            expected = 1,
            actual = messageProducer.calls
        )

        assertEquals(
            expected = Triple(
                MessageTopic.INVOICE_PDF, response.invoiceId.toString(), """
                {
                    "invoiceId": "${response.invoiceId}",
                    "userId": "${userModelFixture.id}",
                    "type": "generate_pdf"
                }
            """.trimIndent()
            ),
            actual = messageProducer.messages.first()
        )
    }

    @Test
    fun `should throw error when due date is past`() = runTest {
        val today = Instant.parse("2000-06-19T00:00:00Z")

        clock.nowResponse = today

        val error = assertFailsWith<HttpError> {
            service.createInvoice(
                BASE_INPUT.copy(
                    dueDate = LocalDate.parse("2000-06-18")
                ),
                userId = UUID.fromString("fed3e1ac-c755-4048-9315-356054c4da11")
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error when issue date is past`() = runTest {
        val today = Instant.parse("2000-06-19T03:00:00Z")

        clock.nowResponse = today

        val error = assertFailsWith<HttpError> {
            service.createInvoice(
                BASE_INPUT.copy(
                    issueDate = LocalDate.parse("2000-06-18")
                ),
                userId = UUID.fromString("fed3e1ac-c755-4048-9315-356054c4da11")
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error when issue date is after due date`() = runTest {

        val error = assertFailsWith<HttpError> {
            service.createInvoice(
                BASE_INPUT.copy(
                    issueDate = LocalDate.parse("2000-06-19"),
                    dueDate = LocalDate.parse("2000-06-18")
                ),
                userId = UUID.fromString("fed3e1ac-c755-4048-9315-356054c4da11")
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error if external id is already in use`() = runTest {
        val today = Instant.parse("2000-06-19T00:00:00Z")
        val invoiceResponse = "fed3e1ac-c755-4048-9315-356054c4da11"

        clock.nowResponse = today

        getCompanyDetailsService.response = company
        customerByIdService.response = customer(company.id)

        invoiceRepository.createInvoiceResponse = { invoiceResponse }
        getUserByIdService.response = { userModelFixture }
        invoiceRepository.getInvoiceByExternalIdResponse = { invoiceModelFixture }

        val error = assertFailsWith<HttpError> {
            clock.nowResponse = today
            invoiceRepository.createInvoiceResponse = { invoiceResponse }
            getUserByIdService.response = { userModelFixture }
            invoiceRepository.getInvoiceByExternalIdResponse = { invoiceModelFixture }

            service.createInvoice(
                BASE_INPUT,
                userId = userModelFixture.id
            )
        }

        assertEquals(
            expected = HttpCode.Conflict,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error if invoice activities is empty`() = runTest {
        val today = Instant.parse("2000-06-19T00:00:00Z")
        val invoiceResponse = "fed3e1ac-c755-4048-9315-356054c4da11"

        val error = assertFailsWith<HttpError> {
            clock.nowResponse = today
            invoiceRepository.createInvoiceResponse = { invoiceResponse }
            getUserByIdService.response = { userModelFixture }
            invoiceRepository.getInvoiceByExternalIdResponse = { null }

            service.createInvoice(
                BASE_INPUT.copy(activities = emptyList()),
                userId = userModelFixture.id
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )

        assertEquals(
            expected = "Invoice must have at least one service",
            actual = error.message
        )
    }

    @Test
    fun `should throw error if any invoice activity has quantity less than 1`() = runTest {
        val today = Instant.parse("2000-06-19T00:00:00Z")
        val invoiceResponse = "fed3e1ac-c755-4048-9315-356054c4da11"

        val error = assertFailsWith<HttpError> {
            clock.nowResponse = today
            invoiceRepository.createInvoiceResponse = { invoiceResponse }
            getUserByIdService.response = { userModelFixture }
            invoiceRepository.getInvoiceByExternalIdResponse = { null }

            service.createInvoice(
                BASE_INPUT.copy(
                    activities = listOf(
                        CreateInvoiceActivityModel(
                            description = "Description",
                            unitPrice = 10L,
                            quantity = 0
                        )
                    )
                ),
                userId = userModelFixture.id
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )

        assertEquals(
            expected = "Invoice activity must have quantity > 0",
            actual = error.message
        )
    }

    @Test
    fun `should throw error if any invoice activity has unit price less than 1`() = runTest {
        val today = Instant.parse("2000-06-19T00:00:00Z")
        val invoiceResponse = "fed3e1ac-c755-4048-9315-356054c4da11"

        val error = assertFailsWith<HttpError> {
            clock.nowResponse = today
            invoiceRepository.createInvoiceResponse = { invoiceResponse }
            getUserByIdService.response = { userModelFixture }
            invoiceRepository.getInvoiceByExternalIdResponse = { null }

            service.createInvoice(
                BASE_INPUT.copy(
                    activities = listOf(
                        CreateInvoiceActivityModel(
                            description = "Description",
                            unitPrice = 0,
                            quantity = 1
                        )
                    )
                ),
                userId = userModelFixture.id
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )

        assertEquals(
            expected = "Invoice activity must have unitPrice > 0",
            actual = error.message
        )
    }

    companion object {
        val company = companyDetailsFixture.copy(
            user = userModelFixture
        )

        val BASE_INPUT = CreateInvoiceDTO(
            invoicerNumber = "1234",
            issueDate = LocalDate.parse("2000-06-19"),
            dueDate = LocalDate.parse("2000-06-20"),
            activities = listOf(
                CreateInvoiceActivityModel(
                    description = "Description",
                    unitPrice = 10L,
                    quantity = 1
                )
            ),
            customerId = customer(company.id).id,
            companyId = company.id,
        )

        fun customer(companyId: UUID) = CustomerModel(
            id = UUID.fromString("fed3e1ac-c755-4048-9315-356054c4da11"),
            name = "Test Customer",
            email = "adawdwadaw@gmail.com",
            phone = "awdadwa",
            companyId = companyId,
            createdAt = Instant.parse("2000-06-19T00:00:00Z"),
            updatedAt = Instant.parse("2000-06-19T00:00:00Z")
        )
    }
}
