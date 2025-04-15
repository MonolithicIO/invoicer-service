package controller.viewmodel.beneficiary

import models.beneficiary.BeneficiaryModel
import models.beneficiary.UserBeneficiaries
import java.util.UUID
import kotlinx.datetime.Instant
import models.fixtures.userModelFixture
import kotlin.test.Test
import kotlin.test.assertEquals

class UserBeneficiariesViewModelTest {

    @Test
    fun `converts UserBeneficiaries to UserBeneficiariesViewModel`() {
        val beneficiary1 = BeneficiaryModel(
            name = "John Doe",
            iban = "DE89370400440532013000",
            swift = "ABCDEF12",
            bankName = "Bank of Test",
            bankAddress = "123 Test Street",
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            createdAt = Instant.parse("2023-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2023-01-02T00:00:00Z"),
            userId = userModelFixture.id
        )
        val beneficiary2 = BeneficiaryModel(
            name = "Jane Doe",
            iban = "FR7630006000011234567890189",
            swift = "GHIJKL34",
            bankName = "Another Bank",
            bankAddress = "456 Another Street",
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
            createdAt = Instant.parse("2023-01-03T00:00:00Z"),
            updatedAt = Instant.parse("2023-01-04T00:00:00Z"),
            userId = userModelFixture.id
        )
        val userBeneficiaries = UserBeneficiaries(
            items = listOf(beneficiary1, beneficiary2),
            itemCount = 2,
            nextPage = null
        )

        val result = userBeneficiaries.toViewModel()

        assertEquals(2, result.itemCount)
        assertEquals(null, result.nextPage)
        assertEquals(2, result.items.size)
        assertEquals("John Doe", result.items[0].name)
        assertEquals("Jane Doe", result.items[1].name)
    }

    @Test
    fun `converts BeneficiaryModel to UserBeneficiaryViewModel`() {
        val beneficiary = BeneficiaryModel(
            name = "John Doe",
            iban = "DE89370400440532013000",
            swift = "ABCDEF12",
            bankName = "Bank of Test",
            bankAddress = "123 Test Street",
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            createdAt = Instant.parse("2023-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2023-01-02T00:00:00Z"),
            userId = userModelFixture.id
        )

        val result = beneficiary.toViewModel()

        assertEquals("John Doe", result.name)
        assertEquals("DE89370400440532013000", result.iban)
        assertEquals("ABCDEF12", result.swift)
        assertEquals("Bank of Test", result.bankName)
        assertEquals("123 Test Street", result.bankAddress)
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.id)
        assertEquals("2023-01-01T00:00:00Z", result.createdAt)
        assertEquals("2023-01-02T00:00:00Z", result.updatedAt)
    }
}