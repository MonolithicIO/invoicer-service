package models.invoice

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import models.company.CompanyDetailsModel
import models.customer.CustomerModel
import java.util.*

data class CreateInvoiceDTO(
    val invoicerNumber: String,
    val activities: List<CreateInvoiceActivityModel>,
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val customerId: UUID,
    val companyId: UUID
)

data class CreateInvoiceModel(
    val customer: CustomerModel,
    val company: CompanyDetailsModel,
    val invoicerNumber: String,
    val activities: List<CreateInvoiceActivityModel>,
    val issueDate: LocalDate,
    val dueDate: LocalDate
)

data class CreateInvoiceActivityModel(
    val description: String,
    val unitPrice: Long,
    val quantity: Int
)
