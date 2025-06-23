package controller.viewmodel.invoice

import kotlinx.datetime.Instant
import models.InvoiceModelLegacy
import models.InvoiceModelActivityModelLegacy
import models.beneficiary.BeneficiaryModel
import models.fixtures.beneficiaryModelFixture
import models.fixtures.intermediaryModelFixture
import models.fixtures.userModelFixture
import models.intermediary.IntermediaryModel
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class InvoiceDetailsViewModelTest {

    @Test
    fun `converts InvoiceModel to InvoiceDetailsViewModel`() {
        val invoiceModelLegacy = InvoiceModelLegacy(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            externalId = "INV-001",
            senderCompanyName = "Sender Company",
            senderCompanyAddress = "123 Sender St",
            recipientCompanyName = "Recipient Company",
            recipientCompanyAddress = "456 Recipient St",
            issueDate = Instant.parse("2023-01-01T00:00:00Z"),
            dueDate = Instant.parse("2023-01-15T00:00:00Z"),
            beneficiary = BeneficiaryModel(
                name = "Beneficiary Name",
                iban = "DE89370400440532013000",
                swift = "ABCDEF12",
                bankName = "Bank of Beneficiary",
                bankAddress = "789 Bank St",
                userId = userModelFixture.id,
                createdAt = Instant.parse("2023-01-01T10:00:00Z"),
                updatedAt = Instant.parse("2023-01-01T10:00:00Z"),
                id = beneficiaryModelFixture.id
            ),
            intermediary = IntermediaryModel(
                name = "Intermediary Name",
                iban = "FR7630006000011234567890189",
                swift = "GHIJKL34",
                bankName = "Bank of Intermediary",
                bankAddress = "101 Intermediary St",
                userId = userModelFixture.id,
                createdAt = Instant.parse("2023-01-01T10:00:00Z"),
                updatedAt = Instant.parse("2023-01-01T10:00:00Z"),
                id = intermediaryModelFixture.id
            ),
            activities = listOf(
                InvoiceModelActivityModelLegacy(
                    id = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
                    name = "Service A",
                    unitPrice = 1000,
                    quantity = 2
                )
            ),
            createdAt = Instant.parse("2023-01-01T10:00:00Z"),
            updatedAt = Instant.parse("2023-01-02T10:00:00Z"),
            user = userModelFixture
        )

        val result = invoiceModelLegacy.toViewModel()

        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.id)
        assertEquals("INV-001", result.externalId)
        assertEquals("Sender Company", result.senderCompany.name)
        assertEquals("123 Sender St", result.senderCompany.address)
        assertEquals("Recipient Company", result.recipientCompany.name)
        assertEquals("456 Recipient St", result.recipientCompany.address)
        assertEquals("2023-01-01T00:00:00Z", result.issueDate)
        assertEquals("2023-01-15T00:00:00Z", result.dueDate)
        assertEquals("Beneficiary Name", result.beneficiary.name)
        assertEquals("DE89370400440532013000", result.beneficiary.iban)
        assertEquals("ABCDEF12", result.beneficiary.swift)
        assertEquals("Bank of Beneficiary", result.beneficiary.bankName)
        assertEquals("789 Bank St", result.beneficiary.bankAddress)
        assertEquals("Intermediary Name", result.intermediary?.name)
        assertEquals("FR7630006000011234567890189", result.intermediary?.iban)
        assertEquals("GHIJKL34", result.intermediary?.swift)
        assertEquals("Bank of Intermediary", result.intermediary?.bankName)
        assertEquals("101 Intermediary St", result.intermediary?.bankAddress)
        assertEquals(1, result.activities.size)
        assertEquals("123e4567-e89b-12d3-a456-426614174001", result.activities[0].id)
        assertEquals("Service A", result.activities[0].description)
        assertEquals(1000, result.activities[0].unitPrice)
        assertEquals(2, result.activities[0].quantity)
        assertEquals("2023-01-01T10:00:00Z", result.createdAt)
        assertEquals("2023-01-02T10:00:00Z", result.updatedAt)
    }

    @Test
    fun `handles empty activities list`() {
        val invoiceModelLegacy = InvoiceModelLegacy(
            id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
            externalId = "INV-002",
            senderCompanyName = "Sender Company",
            senderCompanyAddress = "123 Sender St",
            recipientCompanyName = "Recipient Company",
            recipientCompanyAddress = "456 Recipient St",
            issueDate = Instant.parse("2023-01-01T00:00:00Z"),
            dueDate = Instant.parse("2023-01-15T00:00:00Z"),
            beneficiary = BeneficiaryModel(
                name = "Beneficiary Name",
                iban = "DE89370400440532013000",
                swift = "ABCDEF12",
                bankName = "Bank of Beneficiary",
                bankAddress = "789 Bank St",
                userId = userModelFixture.id,
                createdAt = Instant.parse("2023-01-01T10:00:00Z"),
                updatedAt = Instant.parse("2023-01-01T10:00:00Z"),
                id = beneficiaryModelFixture.id
            ),
            intermediary = null,
            activities = listOf(),
            createdAt = Instant.parse("2023-01-01T10:00:00Z"),
            updatedAt = Instant.parse("2023-01-02T10:00:00Z"),
            user = userModelFixture
        )

        val result = invoiceModelLegacy.toViewModel()

        assertEquals(0, result.activities.size)
    }
}