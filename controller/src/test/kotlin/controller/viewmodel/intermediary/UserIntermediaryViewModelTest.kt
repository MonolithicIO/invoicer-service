package controller.viewmodel.intermediary

import kotlinx.datetime.Instant
import models.fixtures.userModelFixture
import models.intermediary.IntermediaryModel
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class UserIntermediaryViewModelTest {

    @Test
    fun `converts IntermediaryModel to UserIntermediaryViewModel`() {
        val intermediaryModel = IntermediaryModel(
            name = "Intermediary Name",
            iban = "DE89370400440532013000",
            swift = "ABCDEF12",
            bankName = "Bank of Test",
            bankAddress = "123 Test Street",
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            createdAt = Instant.parse("2023-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2023-01-02T00:00:00Z"),
            userId = userModelFixture.id
        )

        val result = intermediaryModel.toViewModel()

        assertEquals("Intermediary Name", result.name)
        assertEquals("DE89370400440532013000", result.iban)
        assertEquals("ABCDEF12", result.swift)
        assertEquals("Bank of Test", result.bankName)
        assertEquals("123 Test Street", result.bankAddress)
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.id)
        assertEquals("2023-01-01T00:00:00Z", result.createdAt)
        assertEquals("2023-01-02T00:00:00Z", result.updatedAt)
    }

    @Test
    fun `converts a list of IntermediaryModel to UserIntermediariesViewModel`() {
        val intermediaryModel1 = IntermediaryModel(
            name = "Intermediary One",
            iban = "DE89370400440532013000",
            swift = "ABCDEF12",
            bankName = "Bank One",
            bankAddress = "123 Test Street",
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            createdAt = Instant.parse("2023-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2023-01-02T00:00:00Z"),
            userId = userModelFixture.id
        )
        val intermediaryModel2 = IntermediaryModel(
            name = "Intermediary Two",
            iban = "FR7630006000011234567890189",
            swift = "GHIJKL34",
            bankName = "Bank Two",
            bankAddress = "456 Another Street",
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
            createdAt = Instant.parse("2023-01-03T00:00:00Z"),
            updatedAt = Instant.parse("2023-01-04T00:00:00Z"),
            userId = userModelFixture.id
        )

        val intermediaries = listOf(intermediaryModel1, intermediaryModel2)
        val result = UserIntermediariesViewModel(intermediaries.map { it.toViewModel() })

        assertEquals(2, result.intermediaries.size)
        assertEquals("Intermediary One", result.intermediaries[0].name)
        assertEquals("Intermediary Two", result.intermediaries[1].name)
    }
}