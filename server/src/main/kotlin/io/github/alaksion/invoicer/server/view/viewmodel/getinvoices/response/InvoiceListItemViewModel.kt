package io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.response

import kotlinx.serialization.Serializable

@Serializable
data class InvoiceListItemViewModel(
    val id: String,
    val externalId: String,
    val senderCompany: String,
    val recipientCompany: String,
    val issueDate: String,
    val dueDate: String,
    val createdAt: String,
    val updatedAt: String,
    val totalAmount: Long,
)
