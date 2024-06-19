package io.github.alaksion.invoicer.server.viewmodel

import io.github.alaksion.invoicer.server.entities.InvoiceActivityEntity
import io.github.alaksion.invoicer.server.entities.InvoiceEntity

data class InvoiceDetailsViewModel(
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
) {
    internal companion object Factory {
        operator fun invoke(entity: InvoiceEntity): InvoiceDetailsViewModel {
            return InvoiceDetailsViewModel(
                id = entity.id.value.toString(),
                externalId = entity.externalId,
                senderCompany = InvoiceDetailsCompanyViewModel(
                    name = entity.senderCompanyName,
                    address = entity.senderCompanyAddress
                ),
                recipientCompany = InvoiceDetailsCompanyViewModel(
                    name = entity.recipientCompanyName,
                    address = entity.recipientCompanyAddress
                ),
                issueDate = entity.issueDate.toString(),
                dueDate = entity.dueDate.toString(),
                beneficiary = InvoiceDetailsTransactionAccountViewModel(
                    name = entity.beneficiaryName,
                    bankName = entity.beneficiaryBankName,
                    bankAddress = entity.beneficiaryBankAddress,
                    swift = entity.beneficiarySwift,
                    iban = entity.beneficiaryIban
                ),
                intermediary = makeIntermediaryBankAccount(entity),
                activities = makeActivities(entity.activities.toList()),
                createdAt = entity.createdAt.toString(),
                updatedAt = entity.updatedAt.toString()
            )
        }
    }
}

data class InvoiceDetailsCompanyViewModel(
    val name: String,
    val address: String
)

data class InvoiceDetailsTransactionAccountViewModel(
    val name: String?,
    val iban: String,
    val swift: String,
    val bankName: String,
    val bankAddress: String,
)

data class InvoiceDetailsActivityViewModel(
    val id: String,
    val description: String,
    val unitPrice: Long,
    val quantity: Int
)

private fun makeIntermediaryBankAccount(entity: InvoiceEntity): InvoiceDetailsTransactionAccountViewModel? {
    return if (
        entity.intermediaryBankName != null &&
        entity.intermediaryBankAddress != null &&
        entity.intermediarySwift != null &&
        entity.intermediaryIban != null
    ) {
        return InvoiceDetailsTransactionAccountViewModel(
            name = null,
            bankName = entity.intermediaryBankName!!,
            bankAddress = entity.intermediaryBankAddress!!,
            swift = entity.intermediarySwift!!,
            iban = entity.intermediaryIban!!
        )
    } else null
}

private fun makeActivities(activities: List<InvoiceActivityEntity>): List<InvoiceDetailsActivityViewModel> {
    return activities.map {
        InvoiceDetailsActivityViewModel(
            id = it.id.value.toString(),
            description = it.description,
            unitPrice = it.unitPrice,
            quantity = it.quantity
        )
    }
}


