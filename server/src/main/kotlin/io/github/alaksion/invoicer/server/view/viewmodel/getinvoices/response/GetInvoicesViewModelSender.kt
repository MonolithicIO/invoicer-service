package io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.response

import io.github.alaksion.invoicer.server.domain.model.getinvoices.InvoiceListItemModel

interface GetInvoicesViewModelSender {
    fun send(model: List<InvoiceListItemModel>): List<InvoiceListItemViewModel>
}

class GetInvoicesViewModelSenderImpl : GetInvoicesViewModelSender {

    override fun send(model: List<InvoiceListItemModel>): List<InvoiceListItemViewModel> {
        return model.map {
            InvoiceListItemViewModel(
                id = it.id.toString(),
                externalId = it.externalId,
                senderCompany = it.senderCompany,
                recipientCompany = it.recipientCompany,
                issueDate = it.issueDate.toString(),
                dueDate = it.dueDate.toString(),
                createdAt = it.createdAt.toString(),
                updatedAt = it.updatedAt.toString(),
                totalAmount = it.totalAmount,
            )
        }
    }
}