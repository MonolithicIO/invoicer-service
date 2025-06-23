package models.invoice

import kotlinx.datetime.Instant
import models.company.CompanyDetailsModel
import models.customer.CustomerModel
import java.util.*

data class CreateInvoiceDTO(
    val invoicerNumber: String,
    val activities: List<CreateInvoiceActivityModel>,
    val issueDate: Instant,
    val dueDate: Instant,
    val customerId: UUID,
    val companyId: UUID
)

data class CreateInvoiceModel(
    val customer: CustomerModel,
    val company: CompanyDetailsModel,
    val invoicerNumber: String,
    val activities: List<CreateInvoiceActivityModel>,
    val issueDate: Instant,
    val dueDate: Instant
)

data class CreateInvoiceActivityModel(
    val description: String,
    val unitPrice: Long,
    val quantity: Int
)
