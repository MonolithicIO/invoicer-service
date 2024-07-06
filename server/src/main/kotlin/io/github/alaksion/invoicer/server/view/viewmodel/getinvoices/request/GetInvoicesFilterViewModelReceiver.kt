package io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.request

import io.github.alaksion.invoicer.server.domain.errors.badRequestError
import io.github.alaksion.invoicer.server.domain.model.getinvoices.GetInvoicesFilterModel
import kotlinx.datetime.LocalDate

interface GetInvoicesFilterViewModelReceiver {
    fun receive(
        model: GetInvoicesFilterViewModel,
    ): GetInvoicesFilterModel
}

internal class GetInvoicesFilterViewModelReceiverImpl : GetInvoicesFilterViewModelReceiver {

    override fun receive(
        model: GetInvoicesFilterViewModel,
    ): GetInvoicesFilterModel {

        return GetInvoicesFilterModel(
            minIssueDate = parseDate(model.minIssueDate, "Invalid date format: minIssueDate"),
            maxIssueDate = parseDate(model.minIssueDate, "Invalid date format: maxIssueDate"),
            minDueDate = parseDate(model.minIssueDate, "Invalid date format: minDueDate"),
            maxDueDate = parseDate(model.minIssueDate, "Invalid date format: maxDueDate"),
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

}