package services.impl.invoice

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import models.InvoiceModel
import models.InvoiceModelActivityModel
import models.beneficiary.BeneficiaryModel
import models.createinvoice.CreateInvoiceActivityModel
import models.createinvoice.CreateInvoiceModel
import models.user.UserModel
import org.junit.Before
import org.junit.Test
import repository.test.repository.FakeInvoiceRepository
import services.test.beneficiary.FakeGetBeneficiaryByIdService
import services.test.intermediary.FakeGetIntermediaryByIdService
import services.test.user.FakeGetUserByIdService
import utils.date.test.FakeDateProvider
import utils.exceptions.HttpCode
import utils.exceptions.HttpError
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateInvoiceServiceImplTest {

    private lateinit var service: CreateInvoiceServiceImpl
    private lateinit var invoiceRepository: FakeInvoiceRepository
    private lateinit var dateProvider: FakeDateProvider
    private lateinit var getUserByIdService: FakeGetUserByIdService
    private lateinit var getBeneficiaryByIdService: FakeGetBeneficiaryByIdService
    private lateinit var getIntermediaryByIdService: FakeGetIntermediaryByIdService

    @Before
    fun setUp() {
        invoiceRepository = FakeInvoiceRepository()
        dateProvider = FakeDateProvider()
        getUserByIdService = FakeGetUserByIdService()
        getBeneficiaryByIdService = FakeGetBeneficiaryByIdService()
        getIntermediaryByIdService = FakeGetIntermediaryByIdService()

        service = CreateInvoiceServiceImpl(
            invoiceRepository = invoiceRepository,
            dateProvider = dateProvider,
            getUserByIdService = getUserByIdService,
            getBeneficiaryByIdService = getBeneficiaryByIdService,
            getIntermediaryByIdService = getIntermediaryByIdService

        )
    }

    @Test
    fun `should throw error when issue date is past`() = runTest {
        val today = LocalDate(2000, 6, 19)

        dateProvider.nowResponse = { today }

        val error = assertFailsWith<HttpError> {
            service.createInvoice(
                BASE_INPUT.copy(
                    issueDate = today.minus(DatePeriod(days = 1))
                ),
                userId = "fed3e1ac-c755-4048-9315-356054c4da11"
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error when due date is past`() = runTest {
        val today = LocalDate(2000, 6, 19)

        dateProvider.nowResponse = { today }

        val error = assertFailsWith<HttpError> {
            service.createInvoice(
                BASE_INPUT.copy(
                    dueDate = today.minus(DatePeriod(days = 1))
                ),
                userId = "fed3e1ac-c755-4048-9315-356054c4da11"
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error when issue date is after due date`() = runTest {
        val today = LocalDate(2000, 6, 19)

        val error = assertFailsWith<HttpError> {
            service.createInvoice(
                BASE_INPUT.copy(
                    dueDate = today,
                    issueDate = today.plus(DatePeriod(days = 1))
                ),
                userId = "fed3e1ac-c755-4048-9315-356054c4da11"
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `should throw error if external id is duplicated`() = runTest {
        invoiceRepository.getInvoiceByExternalIdResponse = { duplicatedInvoice }
    }

    companion object {
        val BASE_INPUT = CreateInvoiceModel(
            externalId = "1234",
            senderCompanyAddress = "Sender Company Address",
            senderCompanyName = "Sender Company name",
            recipientCompanyName = "Recipient Company name",
            recipientCompanyAddress = "Recipient Company Address",
            issueDate = LocalDate(2000, 6, 19),
            dueDate = LocalDate(2000, 6, 19),
            beneficiaryId = "b2db44e1-af93-48cf-94fe-7484fd8045ef",
            intermediaryId = "02cac010-bc14-4192-872f-103f27afa1ed",
            activities = listOf(
                CreateInvoiceActivityModel(
                    description = "Description",
                    unitPrice = 10L,
                    quantity = 1
                )
            )
        )

        val duplicatedInvoice = InvoiceModel(
            id = UUID.fromString("7e1855bd-6e94-413f-bfe7-9bace9f25e7c"),
            externalId = "1234",
            senderCompanyAddress = "Sender Company Address",
            senderCompanyName = "Sender Company name",
            recipientCompanyName = "Recipient Company name",
            recipientCompanyAddress = "Recipient Company Address",
            issueDate = LocalDate(2000, 6, 19),
            dueDate = LocalDate(2000, 6, 19),
            beneficiary = BeneficiaryModel(
                name = "Beneficiary",
                iban = "iban",
                swift = "swift",
                bankName = "bank name",
                bankAddress = "bank address",
                userId = "fed3e1ac-c755-4048-9315-356054c4da11",
                id = "b2db44e1-af93-48cf-94fe-7484fd8045ef",
                createdAt = LocalDate(2000, 6, 19),
                updatedAt = LocalDate(2000, 6, 19)
            ),
            intermediary = null,
            createdAt = LocalDate(2000, 6, 19),
            updatedAt = LocalDate(2000, 6, 19),
            activities = listOf(
                InvoiceModelActivityModel(
                    name = "Description",
                    unitPrice = 10L,
                    quantity = 1,
                    id = UUID.fromString("7e1855bd-6e94-413f-bfe7-9bace9f25e7c"),
                )
            ),
            user = UserModel(
                id = UUID.fromString("fed3e1ac-c755-4048-9315-356054c4da11"),
                email = "email",
                password = "password",
                createdAt = LocalDate(2000, 6, 19),
                updatedAt = LocalDate(2000, 6, 19),
                verified = true,
            )
        )
    }

}