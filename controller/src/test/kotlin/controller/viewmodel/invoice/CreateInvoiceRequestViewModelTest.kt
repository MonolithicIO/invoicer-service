package controller.viewmodel.invoice

import io.github.alaksion.invoicer.utils.uuid.parseUuid
import kotlinx.datetime.Instant
import utils.exceptions.http.HttpError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateInvoiceViewModelTest {

    @Test
    fun `converts to CreateInvoiceModel when all fields are valid`() {
        val viewModel = CreateInvoiceViewModel(
            invoicerNumber = "INV-001",
            senderCompanyName = "Sender Company",
            senderCompanyAddress = "123 Sender St",
            recipientCompanyName = "Recipient Company",
            recipientCompanyAddress = "456 Recipient St",
            issueDate = Instant.parse("2023-01-01T00:00:00Z"),
            dueDate = Instant.parse("2023-01-15T00:00:00Z"),
            beneficiaryId = "123e4567-e89b-12d3-a456-426614174000",
            intermediaryId = "123e4567-e89b-12d3-a456-426614174001",
            activities = listOf(
                CreateInvoiceActivityViewModel(
                    description = "Service A",
                    unitPrice = 1000,
                    quantity = 2
                )
            )
        )

        val result = viewModel.toModel()

        assertEquals("INV-001", result.externalId)
        assertEquals("Sender Company", result.senderCompanyName)
        assertEquals("123 Sender St", result.senderCompanyAddress)
        assertEquals("Recipient Company", result.recipientCompanyName)
        assertEquals("456 Recipient St", result.recipientCompanyAddress)
        assertEquals(Instant.parse("2023-01-01T00:00:00Z"), result.issueDate)
        assertEquals(Instant.parse("2023-01-15T00:00:00Z"), result.dueDate)
        assertEquals(parseUuid("123e4567-e89b-12d3-a456-426614174000"), result.beneficiaryId)
        assertEquals(parseUuid("123e4567-e89b-12d3-a456-426614174001"), result.intermediaryId)
        assertEquals(1, result.activities.size)
        assertEquals("Service A", result.activities[0].description)
        assertEquals(1000, result.activities[0].unitPrice)
        assertEquals(2, result.activities[0].quantity)
    }

    @Test
    fun `throws HttpError when externalId is missing`() {
        val viewModel = CreateInvoiceViewModel(
            senderCompanyName = "Sender Company",
            senderCompanyAddress = "123 Sender St",
            recipientCompanyName = "Recipient Company",
            recipientCompanyAddress = "456 Recipient St",
            issueDate = Instant.parse("2023-01-01T00:00:00Z"),
            dueDate = Instant.parse("2023-01-15T00:00:00Z"),
            beneficiaryId = "123e4567-e89b-12d3-a456-426614174000",
            activities = listOf(
                CreateInvoiceActivityViewModel(
                    description = "Service A",
                    unitPrice = 1000,
                    quantity = 2
                )
            )
        )

        val exception = assertFailsWith<HttpError> { viewModel.toModel() }
        assertEquals("Missing external Id", exception.message)
    }

    @Test
    fun `throws HttpError when activities list is empty`() {
        val viewModel = CreateInvoiceViewModel(
            invoicerNumber = "INV-001",
            senderCompanyName = "Sender Company",
            senderCompanyAddress = "123 Sender St",
            recipientCompanyName = "Recipient Company",
            recipientCompanyAddress = "456 Recipient St",
            issueDate = Instant.parse("2023-01-01T00:00:00Z"),
            dueDate = Instant.parse("2023-01-15T00:00:00Z"),
            beneficiaryId = "123e4567-e89b-12d3-a456-426614174000",
            activities = listOf()
        )

        val exception = assertFailsWith<HttpError> { viewModel.toModel() }
        assertEquals("Invoice must have at least one activity", exception.message)
    }

    @Test
    fun `throws HttpError when activity quantity is negative`() {
        val viewModel = CreateInvoiceViewModel(
            invoicerNumber = "INV-001",
            senderCompanyName = "Sender Company",
            senderCompanyAddress = "123 Sender St",
            recipientCompanyName = "Recipient Company",
            recipientCompanyAddress = "456 Recipient St",
            issueDate = Instant.parse("2023-01-01T00:00:00Z"),
            dueDate = Instant.parse("2023-01-15T00:00:00Z"),
            beneficiaryId = "123e4567-e89b-12d3-a456-426614174000",
            activities = listOf(
                CreateInvoiceActivityViewModel(
                    description = "Service A",
                    unitPrice = 1000,
                    quantity = -1
                )
            )
        )

        val exception = assertFailsWith<HttpError> { viewModel.toModel() }
        assertEquals("Negative activity quantity at index 0", exception.message)
    }

    @Test
    fun `throws HttpError when activity description is missing`() {
        val viewModel = CreateInvoiceViewModel(
            invoicerNumber = "INV-001",
            senderCompanyName = "Sender Company",
            senderCompanyAddress = "123 Sender St",
            recipientCompanyName = "Recipient Company",
            recipientCompanyAddress = "456 Recipient St",
            issueDate = Instant.parse("2023-01-01T00:00:00Z"),
            dueDate = Instant.parse("2023-01-15T00:00:00Z"),
            beneficiaryId = "123e4567-e89b-12d3-a456-426614174000",
            activities = listOf(
                CreateInvoiceActivityViewModel(
                    description = null,
                    unitPrice = 1000,
                    quantity = 2
                )
            )
        )

        val exception = assertFailsWith<HttpError> { viewModel.toModel() }
        assertEquals("Missing activity description at index 0", exception.message)
    }
}