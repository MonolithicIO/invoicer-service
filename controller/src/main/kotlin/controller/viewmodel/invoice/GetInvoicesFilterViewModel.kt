package controller.viewmodel.invoice

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import models.getinvoices.GetInvoicesFilterModel
import utils.exceptions.http.badRequestError

@Serializable
internal data class GetInvoicesFilterViewModel(
    val minIssueDate: String?,
    val maxIssueDate: String?,
    val minDueDate: String?,
    val maxDueDate: String?,
)


internal fun receiveGetInvoicesFilterViewModel(
    model: GetInvoicesFilterViewModel,
): GetInvoicesFilterModel {

    return GetInvoicesFilterModel(
        minIssueDate = parseDate(model.minIssueDate, "Invalid date format: minIssueDate"),
        maxIssueDate = parseDate(model.maxIssueDate, "Invalid date format: maxIssueDate"),
        minDueDate = parseDate(model.minDueDate, "Invalid date format: minDueDate"),
        maxDueDate = parseDate(model.maxDueDate, "Invalid date format: maxDueDate"),
    )
}

private fun parseDate(rawString: String?, errorMessage: String): Instant? {
    return rawString?.let {
        runCatching {
            Instant.parse(rawString)
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