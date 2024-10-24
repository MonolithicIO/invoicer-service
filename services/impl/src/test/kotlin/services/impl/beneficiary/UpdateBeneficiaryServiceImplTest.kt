package services.impl.beneficiary

import foundation.validator.test.FakeIbanValidator
import foundation.validator.test.FakeSwiftValidator
import kotlinx.coroutines.test.runTest
import repository.test.repository.FakeBeneficiaryRepository
import services.test.beneficiary.FakeCheckBeneficiarySwiftAvailableService
import services.test.beneficiary.FakeGetBeneficiaryByIdService
import services.test.user.FakeGetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.HttpError
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UpdateBeneficiaryServiceImplTest {

    private lateinit var serviceImpl: UpdateBeneficiaryServiceImpl
    private lateinit var getBeneficiaryByIdService: FakeGetBeneficiaryByIdService
    private lateinit var swiftAvailableService: FakeCheckBeneficiarySwiftAvailableService
    private lateinit var beneficiaryRepository: FakeBeneficiaryRepository
    private lateinit var ibanValidator: FakeIbanValidator
    private lateinit var swiftValidator: FakeSwiftValidator
    private lateinit var getUserByIdService: FakeGetUserByIdService

    @BeforeTest
    fun setUp() {
        swiftValidator = FakeSwiftValidator()
        ibanValidator = FakeIbanValidator()
        beneficiaryRepository = FakeBeneficiaryRepository()
        swiftAvailableService = FakeCheckBeneficiarySwiftAvailableService()
        getBeneficiaryByIdService = FakeGetBeneficiaryByIdService()
        getUserByIdService = FakeGetUserByIdService()

        serviceImpl = UpdateBeneficiaryServiceImpl(
            getBeneficiaryByIdService = getBeneficiaryByIdService,
            beneficiaryRepository = beneficiaryRepository,
            swiftValidator = swiftValidator,
            ibanValidator = ibanValidator,
            checkBeneficiarySwiftAvailableService = swiftAvailableService,
            getUserByIdService = getUserByIdService
        )
    }

    @Test
    fun `given invalid swift code then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> { }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given invalid iban code then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> { }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given invalid beneficiary name then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> { }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given invalid beneficiary bank name then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> { }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given invalid beneficiary bank address then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> { }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given swift code already in use then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> { }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `valid payload then should update model`() = runTest {

    }

}