package io.github.alaksion.invoicer.server.view.viewmodel.invoicedetails.response

import services.api.model.InvoiceModel
import services.api.model.InvoiceModelActivityModel

interface InvoiceDetailsViewModelSender {
    fun send(model: InvoiceModel): InvoiceDetailsViewModel
}

internal class InvoiceDetailsViewModelSenderImpl : InvoiceDetailsViewModelSender {

    override fun send(model: InvoiceModel): InvoiceDetailsViewModel {
        return InvoiceDetailsViewModel(
            id = model.id.toString(),
            externalId = model.externalId,
            senderCompany = InvoiceDetailsCompanyViewModel(
                name = model.senderCompanyName,
                address = model.senderCompanyAddress
            ),
            recipientCompany = InvoiceDetailsCompanyViewModel(
                name = model.recipientCompanyName,
                address = model.recipientCompanyAddress
            ),
            issueDate = model.issueDate.toString(),
            dueDate = model.dueDate.toString(),
            beneficiary = InvoiceDetailsTransactionAccountViewModel(
                name = model.beneficiary.name,
                bankName = model.beneficiary.bankName,
                bankAddress = model.beneficiary.bankAddress,
                swift = model.beneficiary.swift,
                iban = model.beneficiary.iban
            ),
            intermediary = model.intermediary?.let {
                InvoiceDetailsTransactionAccountViewModel(
                    name = it.name,
                    bankName = it.bankName,
                    bankAddress = it.bankAddress,
                    swift = it.swift,
                    iban = it.iban
                )
            },
            activities = makeActivities(model.activities.toList()),
            createdAt = model.createdAt.toString(),
            updatedAt = model.updatedAt.toString()
        )
    }

    private fun makeActivities(activities: List<InvoiceModelActivityModel>): List<InvoiceDetailsActivityViewModel> {
        return activities.map {
            InvoiceDetailsActivityViewModel(
                id = it.id.toString(),
                description = it.name,
                unitPrice = it.unitPrice,
                quantity = it.quantity
            )
        }
    }

}