package io.github.alaksion.invoicer.server.viewmodel.getinvoices

import io.github.alaksion.invoicer.server.entities.InvoiceEntity
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
data class GetInvoicesResponseViewModel(
    val results: List<InvoiceListItemViewModel>
) {
    internal companion object Factory {
        operator fun invoke(items: List<InvoiceEntity>): GetInvoicesResponseViewModel {
            return transaction {
                GetInvoicesResponseViewModel(
                    results = items.map { entity ->
                        InvoiceListItemViewModel(
                            id = entity.id.toString(),
                            externalId = entity.externalId,
                            senderCompany = entity.senderCompanyName,
                            recipientCompany = entity.recipientCompanyName,
                            issueDate = entity.issueDate.toString(),
                            dueDate = entity.dueDate.toString(),
                            createdAt = entity.createdAt.toString(),
                            updatedAt = entity.updatedAt.toString(),
                            totalAmount = entity.activities
                                .map {
                                    it.unitPrice * it.quantity
                                }
                                .reduce { acc, next -> acc + next }
                        )
                    }
                )
            }
        }
    }
}

@Serializable
data class InvoiceListItemViewModel(
    val id: String,
    val externalId: String,
    val senderCompany: String,
    val recipientCompany: String,
    val issueDate: String,
    val dueDate: String,
    val createdAt: String,
    val updatedAt: String,
    val totalAmount: Long,
)
