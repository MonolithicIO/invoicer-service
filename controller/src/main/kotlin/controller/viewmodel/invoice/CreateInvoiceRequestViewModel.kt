package controller.viewmodel.invoice

import io.github.alaksion.invoicer.server.validation.requireFilledString
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import models.createinvoice.CreateInvoiceActivityModel
import models.createinvoice.CreateInvoiceModel
import utils.exceptions.http.badRequestError

@Serializable
data class CreateInvoiceViewModel(
    val externalId: String? = null,
    val senderCompanyName: String? = null,
    val senderCompanyAddress: String? = null,
    val recipientCompanyName: String? = null,
    val recipientCompanyAddress: String? = null,
    val issueDate: Instant? = null,
    val dueDate: Instant? = null,
    val beneficiaryId: String? = null,
    val intermediaryId: String? = null,
    val activities: List<CreateInvoiceActivityViewModel> = listOf()
)


@Serializable
data class CreateInvoiceActivityViewModel(
    val description: String? = null,
    val unitPrice: Long? = null,
    val quantity: Int? = null
)

fun CreateInvoiceViewModel.toModel(): CreateInvoiceModel {
    return CreateInvoiceModel(
        externalId = externalId.requireFilledString("Missing external Id"),
        senderCompanyName = senderCompanyName.requireFilledString("Missing sender company name"),
        senderCompanyAddress = senderCompanyAddress.requireFilledString("Missing sender company address"),
        recipientCompanyName = recipientCompanyName.requireFilledString("Missing recipient company name"),
        recipientCompanyAddress = recipientCompanyAddress.requireFilledString("Missing recipient company name"),
        issueDate = issueDate ?: badRequestError("Missing issue date"),
        dueDate = dueDate ?: badRequestError("Missing issue date"),
        beneficiaryId = beneficiaryId.requireFilledString("Missing beneficiary id"),
        intermediaryId = intermediaryId,
        activities = receiveActivities(activities),
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