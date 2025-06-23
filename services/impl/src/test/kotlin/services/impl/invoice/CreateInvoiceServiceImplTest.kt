package services.impl.invoice

import io.github.alaksion.invoicer.foundation.messaging.MessageTopic
import io.github.alaksion.invoicer.messaging.fakes.FakeMessageProducer
import io.github.alaksion.invoicer.utils.fakes.FakeClock
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import models.invoice.CreateInvoiceActivityModel
import models.invoice.CreateInvoiceModel
import models.fixtures.invoiceModelLegacyFixture
import models.fixtures.userModelFixture
import org.junit.Before
import org.junit.Test
import repository.fakes.FakeInvoiceRepository
import services.api.fakes.beneficiary.FakeGetBeneficiaryByIdService
import services.api.fakes.intermediary.FakeGetIntermediaryByIdService
import services.api.fakes.user.FakeGetUserByIdService
import utils.exceptions.http.HttpCode
import utils.exceptions.http.HttpError
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.days

class CreateInvoiceServiceImplTest {

    private lateinit var service: CreateInvoiceServiceImpl
    private lateinit var invoiceRepository: FakeInvoiceRepository
    private lateinit var clock: FakeClock
    private lateinit var getUserByIdService: FakeGetUserByIdService
    private lateinit var getBeneficiaryByIdService: FakeGetBeneficiaryByIdService
    private lateinit var getIntermediaryByIdService: FakeGetIntermediaryByIdService
    private lateinit var messageProducer: FakeMessageProducer

    @Before
    fun setUp() {
        invoiceRepository = FakeInvoiceRepository()
        clock = FakeClock()
        getUserByIdService = FakeGetUserByIdService()
        getBeneficiaryByIdService = FakeGetBeneficiaryByIdService()
        getIntermediaryByIdService = FakeGetIntermediaryByIdService()
        messageProducer = FakeMessageProducer()

        service = CreateInvoiceServiceImpl(
            invoiceRepository = invoiceRepository,
            clock = clock,
            getUserByIdService = getUserByIdService,
            getBeneficiaryByIdService = getBeneficiaryByIdService,
            getIntermediaryByIdService = getIntermediaryByIdService,
            messageProducer = messageProducer
        )
    }

    @Test
    fun `should create invoice successfully`() = runTest {
        val today = Instant.parse("2000-06-19T00:00:00Z")
        val invoiceResponse = "fed3e1ac-c755-4048-9315-356054c4da11"

        clock.nowResponse = today
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
    fun `should create invoice without intermediary successfully`() = runTest {
        val today = Instant.parse("2000-06-19T00:00:00Z")
        val invoiceResponse = "fed3e1ac-c755-4048-9315-356054c4da11"

        clock.nowResponse = today
        invoiceRepository.createInvoiceResponse = { invoiceResponse }
        getUserByIdService.response = { userModelFixture }
        invoiceRepository.getInvoiceByExternalIdResponse = { null }

        service.createInvoice(
            BASE_INPUT.copy(intermediaryId = null),
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
        invoiceRepository.createInvoiceResponse = { invoiceResponse }
        getUserByIdService.response = { userModelFixture }
        invoiceRepository.getInvoiceByExternalIdResponse = { null }

        val response = service.createInvoice(
            BASE_INPUT.copy(intermediaryId = null),
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
                    dueDate = today.minus(1.days)
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
        val today = Instant.parse("2000-06-19T00:00:00Z")

        clock.nowResponse = today

        val error = assertFailsWith<HttpError> {
            service.createInvoice(
                BASE_INPUT.copy(
                    issueDate = today.minus(1.days)
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
        val today = Instant.parse("2000-06-19T00:00:00Z")

        val error = assertFailsWith<HttpError> {
            service.createInvoice(
                BASE_INPUT.copy(
                    dueDate = today,
                    issueDate = today.plus(1.days)
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

        val error = assertFailsWith<HttpError> {
            clock.nowResponse = today
            invoiceRepository.createInvoiceResponse = { invoiceResponse }
            getUserByIdService.response = { userModelFixture }
            invoiceRepository.getInvoiceByExternalIdResponse = { invoiceModelLegacyFixture }

            service.createInvoice(
                BASE_INPUT.copy(intermediaryId = null),
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
        val BASE_INPUT = CreateInvoiceModel(
            externalId = "1234",
            senderCompanyAddress = "Sender Company Address",
            senderCompanyName = "Sender Company name",
            recipientCompanyName = "Recipient Company name",
            recipientCompanyAddress = "Recipient Company Address",
            issueDate = Instant.parse("2000-06-19T00:00:00Z"),
            dueDate = Instant.parse("2000-06-20T00:00:00Z"),
            beneficiaryId = UUID.fromString("b2db44e1-af93-48cf-94fe-7484fd8045ef"),
            intermediaryId = UUID.fromString("02cac010-bc14-4192-872f-103f27afa1ed"),
            activities = listOf(
                CreateInvoiceActivityModel(
                    description = "Description",
                    unitPrice = 10L,
                    quantity = 1
                )
            )
        )
    }
}