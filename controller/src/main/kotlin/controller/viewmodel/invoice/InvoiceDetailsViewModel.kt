package controller.viewmodel.invoice

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import models.invoice.InvoiceModel
import models.invoice.InvoiceModelActivityModel

@Serializable
internal data class InvoiceDetailsViewModel(
    val id: String,
    val invoiceNumber: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val issueDate: Instant,
    val dueDate: Instant,
    val activities: List<InvoiceDetailsActivityViewModel>
)

@Serializable
internal data class InvoiceDetailsActivityViewModel(
    val id: String,
    val description: String,
    val unitPrice: Long,
    val quantity: Int
)

internal fun InvoiceModel.toViewModel(): InvoiceDetailsViewModel {
    return InvoiceDetailsViewModel(
        id = id.toString(),
        invoiceNumber = invoicerNumber,
        createdAt = createdAt,
        updatedAt = updatedAt,
        issueDate = issueDate,
        dueDate = dueDate,
        activities = makeActivities(activities)
    )
}

private fun makeActivities(activities: List<InvoiceModelActivityModel>): List<InvoiceDetailsActivityViewModel> {
    return activities.map {
        InvoiceDetailsActivityViewModel(
            id = it.id.toString(),
            description = it.name,
            unitPrice = it.unitPrice,
            quantity = it.quantity
        )
    }
}
