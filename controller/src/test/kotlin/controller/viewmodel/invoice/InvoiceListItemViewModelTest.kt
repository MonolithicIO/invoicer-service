package controller.viewmodel.invoice

import kotlinx.datetime.Instant
import models.getinvoices.InvoiceListItemModelLegacy
import models.getinvoices.InvoiceListModelLegacy
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class InvoiceListViewModelTest {

    @Test
    fun `converts InvoiceListModel to InvoiceListViewModel`() {
        val invoiceListModelLegacy = InvoiceListModelLegacy(
            items = listOf(
                InvoiceListItemModelLegacy(
                    id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                    externalId = "INV-001",
                    senderCompany = "Sender Company",
                    recipientCompany = "Recipient Company",
                    issueDate = Instant.parse("2023-01-01T00:00:00Z"),
                    dueDate = Instant.parse("2023-01-15T00:00:00Z"),
                    createdAt = Instant.parse("2023-01-01T10:00:00Z"),
                    updatedAt = Instant.parse("2023-01-02T10:00:00Z"),
                    totalAmount = 10000
                )
            ),
            totalResults = 1,
            nextPage = null
        )

        val result = invoiceListModelLegacy.toViewModel()

        assertEquals(1, result.items.size)
        assertEquals("123e4567-e89b-12d3-a456-426614174000", result.items[0].id)
        assertEquals("INV-001", result.items[0].externalId)
        assertEquals("Sender Company", result.items[0].senderCompany)
        assertEquals("Recipient Company", result.items[0].recipientCompany)
        assertEquals("2023-01-01T00:00:00Z", result.items[0].issueDate)
        assertEquals("2023-01-15T00:00:00Z", result.items[0].dueDate)
        assertEquals("2023-01-01T10:00:00Z", result.items[0].createdAt)
        assertEquals("2023-01-02T10:00:00Z", result.items[0].updatedAt)
        assertEquals(10000, result.items[0].totalAmount)
        assertEquals(1, result.totalItemCount)
        assertEquals(null, result.nextPage)
    }

    @Test
    fun `handles empty InvoiceListModel`() {
        val invoiceListModelLegacy = InvoiceListModelLegacy(
            items = listOf(),
            totalResults = 0,
            nextPage = null
        )

        val result = invoiceListModelLegacy.toViewModel()

        assertEquals(0, result.items.size)
        assertEquals(0, result.totalItemCount)
        assertEquals(null, result.nextPage)
    }
}