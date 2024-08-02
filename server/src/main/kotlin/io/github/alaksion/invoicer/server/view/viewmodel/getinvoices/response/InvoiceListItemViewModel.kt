package io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.response

import io.github.alaksion.invoicer.server.domain.model.getinvoices.InvoiceListItemModel
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

internal fun List<InvoiceListItemModel>.toViewModel(): List<InvoiceListItemViewModel> {
    return map {
        InvoiceListItemViewModel(
            id = it.id.toString(),
            externalId = it.externalId,
            senderCompany = it.senderCompany,
            recipientCompany = it.recipientCompany,
            issueDate = it.issueDate.toString(),
            dueDate = it.dueDate.toString(),
            createdAt = it.createdAt.toString(),
            updatedAt = it.updatedAt.toString(),
            totalAmount = it.totalAmount,
        )
    }
}
