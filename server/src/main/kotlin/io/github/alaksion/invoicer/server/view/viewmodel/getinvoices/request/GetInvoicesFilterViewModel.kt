package io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.request

import io.github.alaksion.invoicer.server.domain.errors.badRequestError
import io.github.alaksion.invoicer.server.domain.model.getinvoices.GetInvoicesFilterModel
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class GetInvoicesFilterViewModel(
    val minIssueDate: String?,
    val maxIssueDate: String?,
    val minDueDate: String?,
    val maxDueDate: String?,
    val senderCompanyName: String?,
    val recipientCompanyName: String?,
)


fun receiveGetInvoicesFilterViewModel(
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