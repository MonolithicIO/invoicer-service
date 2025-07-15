package io.github.monolithic.invoicer.models.invoice

import io.github.monolithic.invoicer.utils.serialization.JavaUUIDSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class InvoiceModel(
    @Serializable(with = JavaUUIDSerializer::class)
    val id: UUID,
    val invoiceNumber: String,
    val issueDate: LocalDate,
    val dueDate: LocalDate,
    val createdAt: Instant,
    val updatedAt: Instant,
    val isDeleted: Boolean,
    val company: InvoiceCompanyModel,
    val customer: InvoiceCustomerModel,
    val primaryAccount: InvoicePayAccountModel,
    val intermediaryAccount: InvoicePayAccountModel?,
    val activities: List<InvoiceModelActivityModel>,
)

@Serializable
data class InvoicePayAccountModel(
    val swift: String,
    val iban: String,
    val bankName: String,
    val bankAddress: String,
)

@Serializable
data class InvoiceCompanyModel(
    @Serializable(with = JavaUUIDSerializer::class)
    val id: UUID,
    val name: String,
    val document: String,
    val addressLine1: String,
    val addressLine2: String?,
    val city: String,
    val zipCode: String,
    val state: String,
    val countryCode: String,
)

@Serializable
data class InvoiceCustomerModel(
    val name: String,
)

@Serializable
data class InvoiceModelActivityModel(
    @Serializable(with = JavaUUIDSerializer::class) val id: UUID,
    val name: String,
    val unitPrice: Long,
    val quantity: Int
)
