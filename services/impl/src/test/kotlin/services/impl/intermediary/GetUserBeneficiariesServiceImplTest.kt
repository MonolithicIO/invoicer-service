package services.impl.intermediary

import io.github.alaksion.invoicer.foundation.http.HttpCode
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import models.intermediary.IntermediaryModel
import repository.test.repository.FakeIntermediaryRepository
import services.test.user.FakeGetUserByIdService
import utils.exceptions.HttpError
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetUserBeneficiariesServiceImplTest {

    private lateinit var service: GetUserIntermediariesServiceImpl
    private lateinit var intermediaryRepository: FakeIntermediaryRepository
    private lateinit var getUserByIdUseCase: FakeGetUserByIdService

    @BeforeTest
    fun setUp() {
        intermediaryRepository = FakeIntermediaryRepository()
        getUserByIdUseCase = FakeGetUserByIdService()
        service = GetUserIntermediariesServiceImpl(
            repository = intermediaryRepository,
            getUserByIdService = getUserByIdUseCase
        )
    }

    @Test
    fun `when beneficiary list contain unowned item then should throw error`() = runTest {
        val error = assertFailsWith<HttpError> {
            intermediaryRepository.getAllResponse = {
                listOf(
                    IntermediaryModel(
                        name = "Name",
                        iban = "Iban",
                        swift = "Swift,",
                        bankName = "bank name",
                        bankAddress = "bank address",
                        userId = "6da1cca3-6784-4f75-8af8-36390b67a5e0",
                        id = "d593ba02-c2bb-4be8-bd97-e71c02d229d3",
                        createdAt = Instant.parse("2000-06-19T00:00:00Z"),
                        updatedAt = Instant.parse("2000-06-19T00:00:00Z"),
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
            IntermediaryModel(
                name = "Name",
                iban = "Iban",
                swift = "Swift,",
                bankName = "bank name",
                bankAddress = "bank address",
                userId = "6da1cca3-6784-4f75-8af8-36390b67a5e0",
                id = "d593ba02-c2bb-4be8-bd97-e71c02d229d3",
                createdAt = Instant.parse("2000-06-19T00:00:00Z"),
                updatedAt = Instant.parse("2000-06-19T00:00:00Z"),
            )
        )

        intermediaryRepository.getAllResponse = { list }

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