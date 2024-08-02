package io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.request

import io.github.alaksion.invoicer.server.domain.errors.badRequestError
import io.github.alaksion.invoicer.server.domain.model.CreateInvoiceActivityModel
import io.github.alaksion.invoicer.server.domain.model.CreateInvoiceModel
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class CreateInvoiceViewModel(
    val externalId: String? = null,
    val senderCompanyName: String? = null,
    val senderCompanyAddress: String? = null,
    val recipientCompanyName: String? = null,
    val recipientCompanyAddress: String? = null,
    val issueDate: LocalDate? = null,
    val dueDate: LocalDate? = null,
    val beneficiary: CreateInvoiceBeneficiaryViewModel? = null,
    val intermediary: CreateInvoiceIntermediaryViewModel? = null,
    val activities: List<CreateInvoiceActivityViewModel> = listOf()
)

@Serializable
data class CreateInvoiceBeneficiaryViewModel(
    val beneficiaryName: String? = null,
    val beneficiaryIban: String? = null,
    val beneficiarySwift: String? = null,
    val beneficiaryBankName: String? = null,
    val beneficiaryBankAddress: String? = null,
)

@Serializable
data class CreateInvoiceIntermediaryViewModel(
    val intermediaryIban: String? = null,
    val intermediarySwift: String? = null,
    val intermediaryBankName: String? = null,
    val intermediaryBankAddress: String? = null,
)

@Serializable
data class CreateInvoiceActivityViewModel(
    val description: String? = null,
    val unitPrice: Long? = null,
    val quantity: Int? = null
)

fun receiveCreateInvoiceViewModel(viewModel: CreateInvoiceViewModel): CreateInvoiceModel {

    return CreateInvoiceModel(
        externalId = viewModel.externalId ?: badRequestError(message = "Missing external Id"),
        senderCompanyName = viewModel.senderCompanyName ?: badRequestError(message = "Missing sender company name"),
        senderCompanyAddress = viewModel.senderCompanyAddress
            ?: badRequestError(message = "Missing sender company address"),
        recipientCompanyName = viewModel.recipientCompanyName
            ?: badRequestError(message = "Missing recipient company name"),
        recipientCompanyAddress = viewModel.recipientCompanyAddress
            ?: badRequestError(message = "Missing recipient company name"),
        issueDate = viewModel.issueDate ?: badRequestError("Missing issue date"),
        dueDate = viewModel.dueDate ?: badRequestError("Missing issue date"),
        beneficiaryName = viewModel.beneficiary?.beneficiaryName ?: badRequestError("Missing beneficiary name"),
        beneficiaryIban = viewModel.beneficiary.beneficiaryIban ?: badRequestError("Missing beneficiary IBAN"),
        beneficiarySwift = viewModel.beneficiary.beneficiarySwift ?: badRequestError("Missing beneficiary SWIFT"),
        beneficiaryBankName = viewModel.beneficiary.beneficiaryBankName
            ?: badRequestError("Missing beneficiary bank name"),
        beneficiaryBankAddress = viewModel.beneficiary.beneficiaryBankAddress
            ?: badRequestError("Missing beneficiary bank address"),
        intermediaryIban = viewModel.intermediary?.intermediaryIban,
        intermediarySwift = viewModel.intermediary?.intermediarySwift,
        intermediaryBankName = viewModel.intermediary?.intermediaryBankName,
        intermediaryBankAddress = viewModel.intermediary?.intermediaryBankAddress,
        activities = receiveActivities(viewModel.activities),
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