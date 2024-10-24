package controller.viewmodel.getinvoices.request

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import models.getinvoices.GetInvoicesFilterModel
import utils.exceptions.badRequestError

@Serializable
internal data class GetInvoicesFilterViewModel(
    val minIssueDate: String?,
    val maxIssueDate: String?,
    val minDueDate: String?,
    val maxDueDate: String?,
    val senderCompanyName: String?,
    val recipientCompanyName: String?,
)


internal fun receiveGetInvoicesFilterViewModel(
    model: GetInvoicesFilterViewModel,
): GetInvoicesFilterModel {

    return GetInvoicesFilterModel(
        minIssueDate = parseDate(model.minIssueDate, "Invalid date format: minIssueDate"),
        maxIssueDate = parseDate(model.maxIssueDate, "Invalid date format: maxIssueDate"),
        minDueDate = parseDate(model.minDueDate, "Invalid date format: minDueDate"),
        maxDueDate = parseDate(model.maxDueDate, "Invalid date format: maxDueDate"),
        senderCompanyName = model.senderCompanyName,
        recipientCompanyName = model.recipientCompanyName,
    )
}

private fun parseDate(rawString: String?, errorMessage: String): LocalDate? {
    return rawString?.let {
        runCatching {
            LocalDate.parse(rawString)
        }.fold(
            onSuccess = {
                it
            },
            onFailure = {
                badRequestError(errorMessage)
            }
        )
    }
}