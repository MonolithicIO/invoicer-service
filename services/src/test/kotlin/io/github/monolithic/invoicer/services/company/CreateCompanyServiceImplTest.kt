package io.github.monolithic.invoicer.services.company

import io.github.monolithic.invoicer.foundation.exceptions.http.HttpError
import io.github.monolithic.invoicer.models.company.CreateCompanyAddressModel
import io.github.monolithic.invoicer.models.company.CreateCompanyModel
import io.github.monolithic.invoicer.models.company.CreateCompanyPaymentAccountModel
import io.github.monolithic.invoicer.repository.UserCompanyRepository
import io.github.monolithic.invoicer.services.payaccount.CheckPayAccountDocumentInUseService
import io.github.monolithic.invoicer.utils.validation.CountryCodeValidator
import io.github.monolithic.invoicer.utils.validation.IbanValidator
import io.github.monolithic.invoicer.utils.validation.SwiftValidator
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest

class CreateCompanyServiceImplTest {

    private lateinit var service: CreateCompanyServiceImpl
    private lateinit var swiftValidator: SwiftValidator
    private lateinit var ibanValidator: IbanValidator
    private lateinit var countryCodeValidator: CountryCodeValidator
    private lateinit var checkPayAccountDocumentService: CheckPayAccountDocumentInUseService
    private lateinit var userCompanyRepository: UserCompanyRepository

    @BeforeTest
    fun setUp() {
        swiftValidator = mockk()
        ibanValidator = mockk()
        countryCodeValidator = mockk()
        checkPayAccountDocumentService = mockk()
        userCompanyRepository = mockk()

        service = CreateCompanyServiceImpl(
            swiftValidator = swiftValidator,
            ibanValidator = ibanValidator,
            countryCodeValidator = countryCodeValidator,
            checkPayAccountDocumentService = checkPayAccountDocumentService,
            userCompanyRepository = userCompanyRepository
        )
    }

    @Test
    fun `createCompany creates company successfully when all validations pass`() = runTest {
        val userId = UUID.randomUUID()
        val companyId = "company-id"
        val companyData = CreateCompanyModel(
            name = "Test Company",
            document = "123456789",
            address = CreateCompanyAddressModel(
                addressLine1 = "123 Test St",
                addressLine2 = null,
                city = "Test City",
                state = "Test State",
                postalCode = "12345",
                countryCode = "USA"
            ),
            paymentAccount = CreateCompanyPaymentAccountModel(
                swift = "TESTSWIFT",
                iban = "TESTIBAN",
                bankName = "Test Bank",
                bankAddress = "456 Bank St"
            ),
            intermediaryAccount = null
        )

        every { swiftValidator.validate(any()) } returns true
        every { ibanValidator.validate(any()) } returns true
        every { countryCodeValidator.validate(any()) } returns true
        coEvery { checkPayAccountDocumentService.checkSwiftInUse(any()) } returns false
        coEvery { checkPayAccountDocumentService.checkIbanInUse(any()) } returns false
        coEvery { userCompanyRepository.createCompany(companyData, userId) } returns companyId

        val result = service.createCompany(companyData, userId)

        assertEquals(companyId, result)
        coVerify { userCompanyRepository.createCompany(companyData, userId) }
    }

    @Test
    fun `createCompany throws error when SWIFT code is invalid`() = runTest {
        val userId = UUID.randomUUID()
        val companyData = CreateCompanyModel(
            name = "Test Company",
            document = "123456789",
            address = CreateCompanyAddressModel(
                addressLine1 = "123 Test St",
                addressLine2 = null,
                city = "Test City",
                state = "Test State",
                postalCode = "12345",
                countryCode = "USA"
            ),
            paymentAccount = CreateCompanyPaymentAccountModel(
                swift = "INVALID",
                iban = "TESTIBAN",
                bankName = "Test Bank",
                bankAddress = "456 Bank St"
            ),
            intermediaryAccount = null
        )

        every { swiftValidator.validate("INVALID") } returns false

        assertFailsWith<HttpError> {
            service.createCompany(companyData, userId)
        }
    }

    @Test
    fun `createCompany throws error when IBAN is already in use`() = runTest {
        val userId = UUID.randomUUID()
        val companyData = CreateCompanyModel(
            name = "Test Company",
            document = "123456789",
            address = CreateCompanyAddressModel(
                addressLine1 = "123 Test St",
                addressLine2 = null,
                city = "Test City",
                state = "Test State",
                postalCode = "12345",
                countryCode = "USA"
            ),
            paymentAccount = CreateCompanyPaymentAccountModel(
                swift = "TESTSWIFT",
                iban = "TESTIBAN",
                bankName = "Test Bank",
                bankAddress = "456 Bank St"
            ),
            intermediaryAccount = null
        )

        every { swiftValidator.validate(any()) } returns true
        every { ibanValidator.validate(any()) } returns true
        coEvery { checkPayAccountDocumentService.checkSwiftInUse(any()) } returns false
        coEvery { checkPayAccountDocumentService.checkIbanInUse("TESTIBAN") } returns true

        assertFailsWith<HttpError> {
            service.createCompany(companyData, userId)
        }
    }

    @Test
    fun `createCompany throws error when company name is blank`() = runTest {
        val userId = "882e8f3e-3f4b-4d2a-9c3b-1f2e3d4c5b6a"

        every { swiftValidator.validate(any()) } returns true
        every { ibanValidator.validate(any()) } returns true
        coEvery { checkPayAccountDocumentService.checkSwiftInUse(any()) } returns false
        coEvery { checkPayAccountDocumentService.checkIbanInUse(any()) } returns true

        val companyData = CreateCompanyModel(
            name = "",
            document = "123456789",
            address = CreateCompanyAddressModel(
                addressLine1 = "123 Test St",
                addressLine2 = null,
                city = "Test City",
                state = "Test State",
                postalCode = "12345",
                countryCode = "USA"
            ),
            paymentAccount = CreateCompanyPaymentAccountModel(
                swift = "TESTSWIFT",
                iban = "TESTIBAN",
                bankName = "Test Bank",
                bankAddress = "456 Bank St"
            ),
            intermediaryAccount = null
        )

        assertFailsWith<HttpError> {
            service.createCompany(companyData, UUID.fromString(userId))
        }
    }

    @Test
    fun `createCompany throws error when country code is invalid`() = runTest {
        val userId = UUID.randomUUID()
        val companyData = CreateCompanyModel(
            name = "Test Company",
            document = "123456789",
            address = CreateCompanyAddressModel(
                addressLine1 = "123 Test St",
                addressLine2 = null,
                city = "Test City",
                state = "Test State",
                postalCode = "12345",
                countryCode = "INVALID"
            ),
            paymentAccount = CreateCompanyPaymentAccountModel(
                swift = "TESTSWIFT",
                iban = "TESTIBAN",
                bankName = "Test Bank",
                bankAddress = "456 Bank St"
            ),
            intermediaryAccount = null
        )

        every { swiftValidator.validate(any()) } returns true
        every { ibanValidator.validate(any()) } returns true
        every { countryCodeValidator.validate("INVALID") } returns false
        coEvery { checkPayAccountDocumentService.checkSwiftInUse(any()) } returns false
        coEvery { checkPayAccountDocumentService.checkIbanInUse(any()) } returns false

        assertFailsWith<HttpError> {
            service.createCompany(companyData, userId)
        }
    }
}
