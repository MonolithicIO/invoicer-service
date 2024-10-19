package services.api.services.beneficiary

import foundation.validator.test.FakeIbanValidator
import foundation.validator.test.FakeSwiftValidator
import kotlinx.coroutines.test.runTest
import repository.test.repository.FakeBeneficiaryRepository
import repository.test.repository.FakeUserRepository
import services.api.services.user.GetUserByIdService
import services.api.services.user.GetUserByIdServiceImpl
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
    private lateinit var checkSwiftService: CheckBeneficiarySwiftAvailableService
    private lateinit var getUserByIdService: GetUserByIdService
    private lateinit var swiftValidator: FakeSwiftValidator
    private lateinit var ibanValidator: FakeIbanValidator

    @BeforeTest
    fun setUp() {
        userRepository = FakeUserRepository()
        beneficiaryRepository = FakeBeneficiaryRepository()
        swiftValidator = FakeSwiftValidator()
        ibanValidator = FakeIbanValidator()
        checkSwiftService = CheckBeneficiarySwiftAvailableServiceImpl(
            repository = beneficiaryRepository
        )
        getUserByIdService = GetUserByIdServiceImpl(
            userRepository = userRepository
        )
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

        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when iban is invalid`() = runTest {
        val exception = assertFailsWith<HttpError> {

        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when name is invalid`() = runTest {
        val exception = assertFailsWith<HttpError> {

        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when bank name is invalid`() = runTest {
        val exception = assertFailsWith<HttpError> {

        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when bank address is invalid`() = runTest {
        val exception = assertFailsWith<HttpError> {

        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should throw error when swift is already in use`() = runTest {
        val exception = assertFailsWith<HttpError> {

        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = exception.statusCode
        )
    }

    @Test
    fun `should return created beneficiary id`() = runTest {

    }

}