package services.impl.beneficiary

import foundation.validator.test.FakeIbanValidator
import foundation.validator.test.FakeSwiftValidator
import kotlinx.coroutines.test.runTest
import models.beneficiary.CreateBeneficiaryModel
import repository.test.repository.FakeBeneficiaryRepository
import repository.test.repository.FakeUserRepository
import services.test.beneficiary.FakeCheckBeneficiarySwiftAvailableService
import services.test.user.FakeGetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.HttpError
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateBeneficiaryServiceImplTest {

    private lateinit var service: CreateBeneficiaryServiceImpl
    private lateinit var beneficiaryRepository: FakeBeneficiaryRepository
    private lateinit var userRepository: FakeUserRepository
    private lateinit var checkSwiftService: FakeCheckBeneficiarySwiftAvailableService
    private lateinit var getUserByIdService: FakeGetUserByIdService
    private lateinit var swiftValidator: FakeSwiftValidator
    private lateinit var ibanValidator: FakeIbanValidator

    @BeforeTest
    fun setUp() {
        userRepository = FakeUserRepository()
        beneficiaryRepository = FakeBeneficiaryRepository()
        swiftValidator = FakeSwiftValidator()
        ibanValidator = FakeIbanValidator()
        checkSwiftService = FakeCheckBeneficiarySwiftAvailableService()
        getUserByIdService = FakeGetUserByIdService()

        service = CreateBeneficiaryServiceImpl(
            getUserByIdService = getUserByIdService,
            repository = beneficiaryRepository,
            checkSwiftService = checkSwiftService,
            swiftValidator = swiftValidator,
            ibanValidator = ibanValidator
        )
    }

    @Test
    fun `should throw error when swift is invalid`() = runTest {
        val exception = assertFailsWith<HttpError> {
            swiftValidator.response = false

            service.create(
                model = INPUT,
                userId = userId
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when iban is invalid`() = runTest {
        val exception = assertFailsWith<HttpError> {
            ibanValidator.response = false

            service.create(
                model = INPUT,
                userId = userId
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when name is invalid`() = runTest {
        val exception = assertFailsWith<HttpError> {
            service.create(
                model = INPUT.copy(
                    name = ""
                ),
                userId = userId
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when bank name is invalid`() = runTest {
        val exception = assertFailsWith<HttpError> {
            service.create(
                model = INPUT.copy(
                    bankName = ""
                ),
                userId = userId
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when bank address is invalid`() = runTest {
        val exception = assertFailsWith<HttpError> {
            service.create(
                model = INPUT.copy(bankName = ""),
                userId = userId
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when swift is already in use`() = runTest {
        val exception = assertFailsWith<HttpError> {
            checkSwiftService.response = { false }

            service.create(
                model = INPUT,
                userId = userId
            )
        }

        assertEquals(
            expected = HttpCode.Conflict,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should return created beneficiary id`() = runTest {
        beneficiaryRepository.createResponse = { "1234" }

        val response = service.create(
            model = INPUT,
            userId = userId
        )

        assertEquals(
            expected = "1234",
            actual = response
        )
    }

    companion object {
        val userId = "7956749e-9d8b-4ab7-abd1-29f0b7ecb9b8"

        val INPUT = CreateBeneficiaryModel(
            name = "Sample name",
            iban = "9999",
            swift = "VWBGLCELXXX",
            bankName = "Bank of America",
            bankAddress = "Bank address"
        )
    }
}