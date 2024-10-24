package services.impl.beneficiary

import foundation.validator.test.FakeIbanValidator
import foundation.validator.test.FakeSwiftValidator
import kotlinx.coroutines.test.runTest
import models.beneficiary.UpdateBeneficiaryModel
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
    private lateinit var swiftInUseService: FakeCheckBeneficiarySwiftAvailableService
    private lateinit var beneficiaryRepository: FakeBeneficiaryRepository
    private lateinit var ibanValidator: FakeIbanValidator
    private lateinit var swiftValidator: FakeSwiftValidator
    private lateinit var getUserByIdService: FakeGetUserByIdService

    @BeforeTest
    fun setUp() {
        swiftValidator = FakeSwiftValidator()
        ibanValidator = FakeIbanValidator()
        beneficiaryRepository = FakeBeneficiaryRepository()
        swiftInUseService = FakeCheckBeneficiarySwiftAvailableService()
        getBeneficiaryByIdService = FakeGetBeneficiaryByIdService()
        getUserByIdService = FakeGetUserByIdService()

        serviceImpl = UpdateBeneficiaryServiceImpl(
            getBeneficiaryByIdService = getBeneficiaryByIdService,
            beneficiaryRepository = beneficiaryRepository,
            swiftValidator = swiftValidator,
            ibanValidator = ibanValidator,
            checkBeneficiarySwiftAvailableService = swiftInUseService,
            getUserByIdService = getUserByIdService
        )
    }

    @Test
    fun `given invalid swift code then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            mockIdCodes(
                ibanValid = true,
                swiftValid = false
            )

            serviceImpl.execute(
                model = UPDATED_REQUEST,
                beneficiaryId = BENEFICIARY_ID,
                userId = USER_ID
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given invalid iban code then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            mockIdCodes(
                ibanValid = false,
                swiftValid = true
            )


            serviceImpl.execute(
                model = UPDATED_REQUEST,
                beneficiaryId = BENEFICIARY_ID,
                userId = USER_ID
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given invalid beneficiary name then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            mockIdCodes(
                ibanValid = true,
                swiftValid = true
            )

            serviceImpl.execute(
                model = UPDATED_REQUEST.copy(
                    name = ""
                ),
                beneficiaryId = BENEFICIARY_ID,
                userId = USER_ID
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given invalid beneficiary bank name then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            mockIdCodes(
                ibanValid = true,
                swiftValid = true
            )

            serviceImpl.execute(
                model = UPDATED_REQUEST.copy(
                    bankName = ""
                ),
                beneficiaryId = BENEFICIARY_ID,
                userId = USER_ID
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given invalid beneficiary bank address then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            mockIdCodes(
                ibanValid = true,
                swiftValid = true
            )

            serviceImpl.execute(
                model = UPDATED_REQUEST.copy(
                    bankAddress = ""
                ),
                beneficiaryId = BENEFICIARY_ID,
                userId = USER_ID
            )
        }

        assertEquals(
            expected = HttpCode.BadRequest,
            actual = error.statusCode
        )
    }

    @Test
    fun `given swift code already in use then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            mockIdCodes(
                ibanValid = true,
                swiftValid = true
            )

            swiftInUseService.response = { true }

            serviceImpl.execute(
                model = UPDATED_REQUEST,
                beneficiaryId = BENEFICIARY_ID,
                userId = USER_ID
            )
        }

        assertEquals(
            expected = HttpCode.Conflict,
            actual = error.statusCode
        )
    }

    @Test
    fun `valid payload then should update model`() = runTest {
        mockIdCodes(
            ibanValid = true,
            swiftValid = true
        )

        swiftInUseService.response = { false }

        beneficiaryRepository.updateBeneficiaryResponse = {
            BeneficiaryTestData.beneficiary
        }

        val result = serviceImpl.execute(
            model = UPDATED_REQUEST,
            beneficiaryId = BENEFICIARY_ID,
            userId = USER_ID
        )

        assertEquals(
            expected = BeneficiaryTestData.beneficiary,
            actual = result
        )
    }

    private fun mockIdCodes(
        ibanValid: Boolean,
        swiftValid: Boolean
    ) {
        ibanValidator.response = ibanValid
        swiftValidator.response = swiftValid
    }

    companion object {
        val UPDATED_REQUEST = UpdateBeneficiaryModel(
            name = "new name",
            iban = "999",
            swift = "1234",
            bankName = "Bank of America",
            bankAddress = "Bank Address"
        )

        val BENEFICIARY_ID = "aae887c0-732e-42d8-ac79-0d1953a7d3ec"
        val USER_ID = "56de1427-e841-47f5-a14d-d0b9503b5a29"
    }

}