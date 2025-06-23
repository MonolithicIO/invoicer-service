package controller.viewmodel.invoice

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import models.invoice.InvoiceListModel

@Serializable
internal data class InvoiceListViewModel(
    val items: List<InvoiceListItemViewModel>,
    val totalItemCount: Long,
    val nextPage: Long?
)

@Serializable
internal data class InvoiceListItemViewModel(
    val id: String,
    val externalId: String,
    val issueDate: Instant,
    val dueDate: Instant,
    val createdAt: Instant,
    val updatedAt: Instant,
    val totalAmount: Long,
    val companyName: String,
    val customerName: String
)

internal fun InvoiceListModel.toViewModel(): InvoiceListViewModel {
    return InvoiceListViewModel(
        items = items.map {
            InvoiceListItemViewModel(
                id = it.id.toString(),
                externalId = it.invoiceNumber,
                companyName = it.companyName,
                customerName = it.customerName,
                issueDate = it.issueDate,
                dueDate = it.dueDate,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                totalAmount = it.totalAmount,
            )
        },
        totalItemCount = totalCount,
        nextPage = nextPage
    )
}
