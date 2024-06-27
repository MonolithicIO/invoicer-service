package io.github.alaksion.invoicer.server.viewmodel.getinvoices

data class GetInvoicesFilterViewModel(
    val minIssueDate: String?,
    val maxIssueDate: String?,
    val minDueDate: String?,
    val maxDueDate: String?,
    val senderCompanyName: String?,
    val recipientCompanyName: String?,
)
