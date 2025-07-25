package io.github.monolithic.invoicer.services.invoice

import io.github.monolithic.invoicer.services.company.GetCompanyDetailsService
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import io.mockk.coEvery
import io.mockk.mockk
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import io.github.monolithic.invoicer.models.fixtures.companyDetailsFixture
import io.github.monolithic.invoicer.models.fixtures.invoiceModelFixture
import io.github.monolithic.invoicer.models.fixtures.userModelFixture
import io.github.monolithic.invoicer.repository.InvoiceRepository

class GetUserInvoiceByIdServiceImplTest {

    private lateinit var service: GetUserInvoiceByIdServiceImpl
    private val repository: InvoiceRepository = mockk()
    private val getUserByIdService: GetUserByIdService = mockk()
    private val companyDetailsService: GetCompanyDetailsService = mockk()


    @BeforeTest
    fun setUp() {
        service = GetUserInvoiceByIdServiceImpl(
            repository = repository,
            getCompanyDetailsService = companyDetailsService,
            getUserService = getUserByIdService,
        )
    }

    @Test
    fun `should successfully return invoice`() = runTest {
        val invoice = invoiceModelFixture.copy(
            company = invoiceModelFixture.company.copy(
                id = companyDetailsFixture.id
            )
        )

        coEvery { getUserByIdService.get(any()) } returns userModelFixture

        coEvery { companyDetailsService.get(any()) } returns
                companyDetailsFixture.copy(user = userModelFixture)

        coEvery { repository.getById(any()) } returns invoice

        val result = service.get(
            invoiceId = invoiceModelFixture.id,
            companyId = companyDetailsFixture.id,
            userId = userModelFixture.id
        )

        assertEquals(
            expected = invoice,
            actual = result
        )
    }
}
