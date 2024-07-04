package io.github.alaksion.invoicer.server.view.viewmodel.getinvoices

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
