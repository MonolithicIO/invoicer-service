package io.github.monolithic.invoicer.models.invoice

import io.github.monolithic.invoicer.models.company.CompanyDetailsModel
import io.github.monolithic.invoicer.models.customer.CustomerModel
import java.util.*
import kotlinx.datetime.LocalDate

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
