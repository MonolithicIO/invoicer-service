package services.impl.beneficiary

import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import models.beneficiary.BeneficiaryModel
import repository.test.repository.FakeBeneficiaryRepository
import services.test.user.FakeGetUserByIdService
import utils.exceptions.HttpCode
import utils.exceptions.HttpError
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetUserBeneficiariesServiceImplTest {

    private lateinit var service: GetUserBeneficiariesServiceImpl
    private lateinit var beneficiaryRepository: FakeBeneficiaryRepository
    private lateinit var getUserByIdUseCase: FakeGetUserByIdService

    @BeforeTest
    fun setUp() {
        beneficiaryRepository = FakeBeneficiaryRepository()
        getUserByIdUseCase = FakeGetUserByIdService()
        service = GetUserBeneficiariesServiceImpl(
            repository = beneficiaryRepository,
            getUserByIdUseCase = getUserByIdUseCase
        )
    }

    @Test
    fun `when beneficiary list contain unowned item then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            beneficiaryRepository.getAllResponse = {
                listOf(
                    BeneficiaryModel(
                        name = "Name",
                        iban = "Iban",
                        swift = "Swift,",
                        bankName = "bank name",
                        bankAddress = "bank address",
                        userId = "6da1cca3-6784-4f75-8af8-36390b67a5e0",
                        id = "d593ba02-c2bb-4be8-bd97-e71c02d229d3",
                        createdAt = LocalDate(2000, 6, 19),
                        updatedAt = LocalDate(2000, 6, 19)
                    )
                )
            }

            service.execute(
                userId = "7933a470-f38d-4e4e-97b2-230cf9fb60b4",
                page = 1,
                limit = 10
            )
        }

        assertEquals(
            expected = HttpCode.Forbidden,
            actual = error.statusCode
        )
    }

    @Test
    fun `should return user beneficiary list`() = runTest {
        val list = listOf(
            BeneficiaryModel(
                name = "Name",
                iban = "Iban",
                swift = "Swift,",
                bankName = "bank name",
                bankAddress = "bank address",
                userId = "6da1cca3-6784-4f75-8af8-36390b67a5e0",
                id = "d593ba02-c2bb-4be8-bd97-e71c02d229d3",
                createdAt = LocalDate(2000, 6, 19),
                updatedAt = LocalDate(2000, 6, 19)
            )
        )

        beneficiaryRepository.getAllResponse = { list }

        val response = service.execute(
            userId = "6da1cca3-6784-4f75-8af8-36390b67a5e0",
            page = 1,
            limit = 10
        )

        assertEquals(
            expected = list,
            actual = response
        )
    }
}