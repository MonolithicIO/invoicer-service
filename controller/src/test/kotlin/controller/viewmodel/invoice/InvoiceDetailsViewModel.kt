package controller.viewmodel.invoice

import kotlinx.serialization.Serializable
import models.InvoiceModel
import models.InvoiceModelActivityModel

@Serializable
internal data class InvoiceDetailsViewModel(
    val id: String,
    val externalId: String,
    val senderCompany: InvoiceDetailsCompanyViewModel,
    val recipientCompany: InvoiceDetailsCompanyViewModel,
    val issueDate: String,
    val dueDate: String,
    val beneficiary: InvoiceDetailsTransactionAccountViewModel,
    val intermediary: InvoiceDetailsTransactionAccountViewModel?,
    val createdAt: String,
    val updatedAt: String,
    val activities: List<InvoiceDetailsActivityViewModel>
)

@Serializable
internal data class InvoiceDetailsCompanyViewModel(
    val name: String,
    val address: String
)

@Serializable
internal data class InvoiceDetailsTransactionAccountViewModel(
    val name: String?,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
)

@Serializable
internal data class InvoiceDetailsActivityViewModel(
    val id: String,
    val description: String,
    val unitPrice: Long,
    val quantity: Int
)

internal fun InvoiceModel.toViewModel(): InvoiceDetailsViewModel {
    return InvoiceDetailsViewModel(
        id = this.id.toString(),
        externalId = this.externalId,
        senderCompany = InvoiceDetailsCompanyViewModel(
            name = this.senderCompanyName,
            address = this.senderCompanyAddress
        ),
        recipientCompany = InvoiceDetailsCompanyViewModel(
            name = this.recipientCompanyName,
            address = this.recipientCompanyAddress
        ),
        issueDate = this.issueDate.toString(),
        dueDate = this.dueDate.toString(),
        beneficiary = InvoiceDetailsTransactionAccountViewModel(
            name = this.beneficiary.name,
            bankName = this.beneficiary.bankName,
            bankAddress = this.beneficiary.bankAddress,
            swift = this.beneficiary.swift,
            iban = this.beneficiary.iban
        ),
        intermediary = this.intermediary?.let {
            InvoiceDetailsTransactionAccountViewModel(
                name = it.name,
                bankName = it.bankName,
                bankAddress = it.bankAddress,
                swift = it.swift,
                iban = it.iban
            )
        },
        activities = makeActivities(this.activities.toList()),
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
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
