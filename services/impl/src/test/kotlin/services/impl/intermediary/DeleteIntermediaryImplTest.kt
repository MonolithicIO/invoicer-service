package services.impl.intermediary

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import models.getinvoices.InvoiceListItemModel
import repository.test.repository.FakeIntermediaryRepository
import repository.test.repository.FakeInvoiceRepository
import services.test.intermediary.FakeGetIntermediaryByIdService
import services.test.user.FakeGetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.HttpError
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeleteIntermediaryImplTest {

    private lateinit var service: DeleteIntermediaryServiceImpl
    private lateinit var getIntermediaryByIdService: FakeGetIntermediaryByIdService
    private lateinit var repository: FakeIntermediaryRepository
    private lateinit var invoiceRepository: FakeInvoiceRepository
    private lateinit var getUserByIdService: FakeGetUserByIdService

    @BeforeTest
    fun setUp() {
        getIntermediaryByIdService = FakeGetIntermediaryByIdService()
        repository = FakeIntermediaryRepository()
        getUserByIdService = FakeGetUserByIdService()
        invoiceRepository = FakeInvoiceRepository()
        service = DeleteIntermediaryServiceImpl(
            getIntermediaryByIdService = getIntermediaryByIdService,
            intermediaryRepo = repository,
            invoiceRepository = invoiceRepository,
            getUserByIdUseCase = getUserByIdService
        )
    }

    @Test
    fun `given no invoices attached to beneficiary then should delete beneficiary`() = runTest {
        invoiceRepository.getInvoicesByIntermediaryIdResponse = { listOf() }

        service.execute(
            beneficiaryId = "988b016b-41a9-487f-b280-283faff1d1d1",
            userId = "b0a7e0bc-044a-42d1-9cc9-f0b63f7f3f36"
        )

        assertEquals(
            expected = 1,
            actual = repository.deleteCalls
        )
    }

    @Test
    fun `given invoices attached to beneficiary then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            invoiceRepository.getInvoicesByIntermediaryIdResponse = {
                listOf(
                    InvoiceListItemModel(
                        id = UUID.fromString("b0a7e0bc-044a-42d1-9cc9-f0b63f7f3f36"),
                        externalId = "123",
                        senderCompany = "Sender company",
                        recipientCompany = "Recipient company",
                        issueDate = LocalDate(2000, 6, 19),
                        dueDate = LocalDate(2000, 6, 20),
                        createdAt = LocalDate(2000, 6, 19),
                        updatedAt = LocalDate(2000, 6, 19),
                        totalAmount = 10
                    )
                )
            }

            service.execute(
                beneficiaryId = "988b016b-41a9-487f-b280-283faff1d1d1",
                userId = "b0a7e0bc-044a-42d1-9cc9-f0b63f7f3f36"
            )
        }

        assertEquals(
            expected = HttpCode.Conflict,
            actual = error.statusCode
        )
    }
}