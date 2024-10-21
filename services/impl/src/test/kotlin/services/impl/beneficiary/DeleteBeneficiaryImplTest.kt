package services.impl.beneficiary

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import models.getinvoices.InvoiceListItemModel
import repository.test.repository.FakeBeneficiaryRepository
import repository.test.repository.FakeInvoiceRepository
import services.test.beneficiary.FakeGetBeneficiaryByIdService
import services.test.user.FakeGetUserByIdService
import utils.exceptions.HttpError
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DeleteBeneficiaryImplTest {

    private lateinit var service: DeleteBeneficiaryServiceImpl
    private lateinit var getBeneficiaryByIdService: FakeGetBeneficiaryByIdService
    private lateinit var repository: FakeBeneficiaryRepository
    private lateinit var invoiceRepository: FakeInvoiceRepository
    private lateinit var getUserByIdService: FakeGetUserByIdService

    @BeforeTest
    fun setUp() {
        getBeneficiaryByIdService = FakeGetBeneficiaryByIdService()
        repository = FakeBeneficiaryRepository()
        getUserByIdService = FakeGetUserByIdService()
        invoiceRepository = FakeInvoiceRepository()
        service = DeleteBeneficiaryServiceImpl(
            getBeneficiaryByIdService = getBeneficiaryByIdService,
            beneficiaryRepository = repository,
            getUserByIdService = getUserByIdService,
            invoiceRepository = invoiceRepository
        )
    }

    @Test
    fun `given no invoices attached to beneficiary then should delete beneficiary`() = runTest {
        invoiceRepository.getInvoicesByBeneficiaryIdResponse = { listOf() }

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
        assertFailsWith<HttpError> {
            invoiceRepository.getInvoicesByBeneficiaryIdResponse = {
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
    }
}