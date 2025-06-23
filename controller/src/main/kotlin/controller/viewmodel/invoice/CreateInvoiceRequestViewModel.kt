package controller.viewmodel.invoice

import controller.validation.requiredString
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import models.invoice.CreateInvoiceActivityModel
import models.invoice.CreateInvoiceDTO
import utils.exceptions.http.badRequestError

@Serializable
data class CreateInvoiceViewModel(
    val invoicerNumber: String? = null,
    val issueDate: Instant? = null,
    val dueDate: Instant? = null,
    val activities: List<CreateInvoiceActivityViewModel> = listOf(),
)


@Serializable
data class CreateInvoiceActivityViewModel(
    val description: String? = null,
    val unitPrice: Long? = null,
    val quantity: Int? = null
)

fun CreateInvoiceViewModel.toModel(
    customerId: String? = null,
    companyId: String? = null
): CreateInvoiceDTO {
    return CreateInvoiceDTO(
        issueDate = issueDate ?: badRequestError("Missing issue date"),
        dueDate = dueDate ?: badRequestError("Missing issue date"),
        activities = receiveActivities(activities),
        customerId = parseUuid(requiredString(customerId, "invalid customer id")),
        companyId = parseUuid(requiredString(companyId, "invalid customer id")),
        invoicerNumber = requiredString(invoicerNumber, "Missing invoice number")
    )
}

private fun receiveActivities(activities: List<CreateInvoiceActivityViewModel>): List<CreateInvoiceActivityModel> {
    if (activities.isEmpty()) badRequestError("Invoice must have at least one activity")

    return activities.mapIndexed { index, activityViewModel ->
        if (activityViewModel.quantity == null) {
            badRequestError("Missing activity quantity at index $index")
        }
        if (activityViewModel.quantity < 0) {
            badRequestError("Negative activity quantity at index $index")
        }

        if (activityViewModel.unitPrice == null) badRequestError("Missing activity unit price at index $index")
        if (activityViewModel.unitPrice < 0) {
            badRequestError("Negative activity unit price at index $index")
        }
        if (activityViewModel.description.isNullOrBlank()) badRequestError("Missing activity description at index $index")

        CreateInvoiceActivityModel(
            description = activityViewModel.description,
            unitPrice = activityViewModel.unitPrice,
            quantity = activityViewModel.quantity
        )
    }
}