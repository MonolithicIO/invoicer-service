package controller.viewmodel.invoice

import kotlinx.datetime.Instant
import utils.exceptions.http.HttpError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GetInvoicesFilterViewModelTest {

    @Test
    fun `converts GetInvoicesFilterViewModel to GetInvoicesFilterModel with valid data`() {
        val viewModel = GetInvoicesFilterViewModel(
            minIssueDate = "2023-01-01T00:00:00Z",
            maxIssueDate = "2023-01-31T23:59:59Z",
            minDueDate = "2023-02-01T00:00:00Z",
            maxDueDate = "2023-02-28T23:59:59Z",
            senderCompanyName = "Sender Company",
            recipientCompanyName = "Recipient Company"
        )

        val result = receiveGetInvoicesFilterViewModel(viewModel)

        assertEquals(Instant.parse("2023-01-01T00:00:00Z"), result.minIssueDate)
        assertEquals(Instant.parse("2023-01-31T23:59:59Z"), result.maxIssueDate)
        assertEquals(Instant.parse("2023-02-01T00:00:00Z"), result.minDueDate)
        assertEquals(Instant.parse("2023-02-28T23:59:59Z"), result.maxDueDate)
        assertEquals("Sender Company", result.senderCompanyName)
        assertEquals("Recipient Company", result.recipientCompanyName)
    }

    @Test
    fun `throws HttpError for invalid minIssueDate format`() {
        val viewModel = GetInvoicesFilterViewModel(
            minIssueDate = "invalid-date",
            maxIssueDate = "2023-01-31T23:59:59Z",
            minDueDate = "2023-02-01T00:00:00Z",
            maxDueDate = "2023-02-28T23:59:59Z",
            senderCompanyName = "Sender Company",
            recipientCompanyName = "Recipient Company"
        )

        val exception = assertFailsWith<HttpError> { receiveGetInvoicesFilterViewModel(viewModel) }
        assertEquals("Invalid date format: minIssueDate", exception.message)
    }

    @Test
    fun `returns null for missing optional date fields`() {
        val viewModel = GetInvoicesFilterViewModel(
            minIssueDate = null,
            maxIssueDate = null,
            minDueDate = null,
            maxDueDate = null,
            senderCompanyName = "Sender Company",
            recipientCompanyName = "Recipient Company"
        )

        val result = receiveGetInvoicesFilterViewModel(viewModel)

        assertEquals(null, result.minIssueDate)
        assertEquals(null, result.maxIssueDate)
        assertEquals(null, result.minDueDate)
        assertEquals(null, result.maxDueDate)
        assertEquals("Sender Company", result.senderCompanyName)
        assertEquals("Recipient Company", result.recipientCompanyName)
    }

    @Test
    fun `throws HttpError for invalid maxDueDate format`() {
        val viewModel = GetInvoicesFilterViewModel(
            minIssueDate = "2023-01-01T00:00:00Z",
            maxIssueDate = "2023-01-31T23:59:59Z",
            minDueDate = "2023-02-01T00:00:00Z",
            maxDueDate = "invalid-date",
            senderCompanyName = "Sender Company",
            recipientCompanyName = "Recipient Company"
        )

        val exception = assertFailsWith<HttpError> { receiveGetInvoicesFilterViewModel(viewModel) }
        assertEquals("Invalid date format: maxDueDate", exception.message)
    }
}