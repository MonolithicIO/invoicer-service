package models.fixtures

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import models.invoice.*
import java.util.*

val invoiceModelActivityFixture = InvoiceModelActivityModel(
    id = UUID.fromString("5d4c54df-fcec-4155-baf7-652aa378071b"),
    name = "Consulting Service",
    unitPrice = 10000L,
    quantity = 2
)

val invoiceModelFixture = InvoiceModel(
    id = UUID.fromString("37f3ef47-5651-49b9-890e-2bc5943bfae4"),
    invoiceNumber = "INV-123456",
    issueDate = LocalDate.parse("2023-01-01"),
    dueDate = LocalDate.parse("2023-01-31"),
    createdAt = Instant.parse("2023-01-01T00:00:00Z"),
    updatedAt = Instant.parse("2023-01-01T00:00:00Z"),
    activities = listOf(invoiceModelActivityFixture),
    isDeleted = false,
    company = InvoiceCompanyModel(
        document = "1234",
        addressLine1 = "Address Line 1",
        addressLine2 = "Address Line 2",
        city = "City",
        zipCode = "12345",
        state = "State",
        countryCode = "US",
        id = UUID.fromString("b23899e4-63b5-42a9-9016-4647d9ec2936"),
        name = "Sender Company Ltd.",
    ),
    customer = InvoiceCustomerModel(
        name = "Ambush"
    ),
    primaryAccount = InvoicePayAccountModel(
        iban = "DE89370400440532013000",
        swift = "COBADEFFXXX",
        bankName = "Bank Name",
        bankAddress = "Bank Address",
    ),
    intermediaryAccount = null,
)

val invoiceListItemModel = InvoiceListItemModel(
    id = UUID.fromString("5d4c54df-fcec-4155-baf7-652aa378071b"),
    invoiceNumber = "INV-123456",
    companyName = "Sender Company Ltd.",
    customerName = "Recipient Company Ltd.",
    issueDate = LocalDate.parse("2023-01-01"),
    dueDate = LocalDate.parse("2023-01-31"),
    createdAt = Instant.parse("2023-01-01T00:00:00Z"),
    updatedAt = Instant.parse("2023-01-01T00:00:00Z"),
    totalAmount = 20000L
)

val invoiceListFixture = InvoiceListModel(
    items = listOf(
        InvoiceListItemModel(
            id = UUID.fromString("5d4c54df-fcec-4155-baf7-652aa378071b"),
            invoiceNumber = "INV-123456",
            companyName = "Sender Company Ltd.",
            customerName = "Recipient Company Ltd.",
            issueDate = LocalDate.parse("2023-01-01"),
            dueDate = LocalDate.parse("2023-01-31"),
            createdAt = Instant.parse("2023-01-01T00:00:00Z"),
            updatedAt = Instant.parse("2023-01-01T00:00:00Z"),
            totalAmount = 20000L
        )
    ),
    totalCount = 1L,
    nextPage = null
)