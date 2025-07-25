package io.github.monolithic.invoicer.controller.viewmodel.invoice

import io.github.monolithic.invoicer.controller.validation.requiredString
import io.github.monolithic.invoicer.utils.uuid.parseUuid
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import io.github.monolithic.invoicer.models.invoice.CreateInvoiceActivityModel
import io.github.monolithic.invoicer.models.invoice.CreateInvoiceDTO
import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError

@Serializable
internal data class CreateInvoiceViewModel(
    val invoiceNumber: String? = null,
    val issueDate: LocalDate? = null,
    val customerId: String? = null,
    val dueDate: LocalDate? = null,
    val activities: List<CreateInvoiceActivityViewModel> = listOf(),
)

@Serializable
internal data class CreateInvoiceActivityViewModel(
    val description: String? = null,
    val unitPrice: Long? = null,
    val quantity: Int? = null
)

internal fun CreateInvoiceViewModel.toModel(
    companyId: String? = null
): CreateInvoiceDTO {
    return CreateInvoiceDTO(
        issueDate = issueDate ?: badRequestError("Missing issue date"),
        dueDate = dueDate ?: badRequestError("Missing issue date"),
        activities = receiveActivities(activities),
        customerId = parseUuid(requiredString(customerId, "invalid customer id")),
        companyId = parseUuid(requiredString(companyId, "invalid customer id")),
        invoicerNumber = requiredString(invoiceNumber, "Missing invoice number")
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

        if (activityViewModel.unitPrice == null)
            badRequestError("Missing activity unit price at index $index")

        if (activityViewModel.unitPrice < 0)
            badRequestError("Negative activity unit price at index $index")

        if (activityViewModel.description.isNullOrBlank())
            badRequestError("Missing activity description at index $index")

        CreateInvoiceActivityModel(
            description = activityViewModel.description,
            unitPrice = activityViewModel.unitPrice,
            quantity = activityViewModel.quantity
        )
    }
}

@Serializable
internal data class CreateInvoiceResponseViewModel(
    val invoiceId: String,
    val externalInvoiceId: String
)
