package io.github.alaksion.invoicer.server.view.viewmodel.invoicedetails.response

import io.github.alaksion.invoicer.server.domain.model.InvoiceModel
import io.github.alaksion.invoicer.server.domain.model.InvoiceModelActivityModel

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
                name = model.beneficiaryName,
                bankName = model.beneficiaryBankName,
                bankAddress = model.beneficiaryBankAddress,
                swift = model.beneficiarySwift,
                iban = model.beneficiaryIban
            ),
            intermediary = makeIntermediaryBankAccount(model),
            activities = makeActivities(model.activities.toList()),
            createdAt = model.createdAt.toString(),
            updatedAt = model.updatedAt.toString()
        )
    }

    private fun makeIntermediaryBankAccount(entity: InvoiceModel): InvoiceDetailsTransactionAccountViewModel? {
        return if (
            entity.intermediaryBankName != null &&
            entity.intermediaryBankAddress != null &&
            entity.intermediarySwift != null &&
            entity.intermediaryIban != null
        ) {
            return InvoiceDetailsTransactionAccountViewModel(
                name = null,
                bankName = entity.intermediaryBankName,
                bankAddress = entity.intermediaryBankAddress,
                swift = entity.intermediarySwift,
                iban = entity.intermediaryIban
            )
        } else null
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