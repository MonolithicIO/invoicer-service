package io.github.alaksion.invoicer.server.repository

import io.github.alaksion.invoicer.server.entities.InvoiceEntity
import io.github.alaksion.invoicer.server.entities.InvoiceTable
import io.github.alaksion.invoicer.server.viewmodel.createinvoice.CreateInvoiceViewModel
import io.github.alaksion.invoicer.server.util.DateProvider
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

internal interface InvoiceRepository {
    suspend fun createInvoice(model: CreateInvoiceViewModel): String
    suspend fun getInvoiceByExternalId(externalId: String): InvoiceEntity?
    suspend fun getInvoiceById(
        id: UUID,
        eagerLoadActivities: Boolean
    ): InvoiceEntity?
}

internal class InvoiceRepositoryImpl(
    private val dateProvider: DateProvider
) : InvoiceRepository {

    override suspend fun createInvoice(model: CreateInvoiceViewModel): String {
        return transaction {
            InvoiceEntity.new {
                externalId = model.externalId
                externalId = model.externalId
                senderCompanyName = model.senderCompanyName
                senderCompanyAddress = model.senderCompanyName
                recipientCompanyName = model.recipientCompanyName
                recipientCompanyAddress = model.recipientCompanyAddress
                issueDate = model.issueDate
                dueDate = model.dueDate
                beneficiaryName = model.beneficiary.beneficiaryName
                beneficiaryIban = model.beneficiary.beneficiaryIban
                beneficiarySwift = model.beneficiary.beneficiarySwift
                beneficiaryBankName = model.beneficiary.beneficiaryBankName
                beneficiaryBankAddress = model.beneficiary.beneficiaryBankAddress

                intermediaryIban = model.intermediary?.intermediaryIban
                intermediarySwift = model.intermediary?.intermediarySwift
                intermediaryBankName = model.intermediary?.intermediaryBankName
                intermediaryBankAddress = model.intermediary?.intermediaryBankAddress
                createdAt = dateProvider.now()
                updatedAt = dateProvider.now()
            }
        }.id.value.toString()
    }

    override suspend fun getInvoiceByExternalId(externalId: String): InvoiceEntity? {
        return transaction {
            InvoiceEntity.find {
                InvoiceTable.externalId eq externalId
            }.firstOrNull()
        }
    }

    override suspend fun getInvoiceById(
        id: UUID,
        eagerLoadActivities: Boolean
    ): InvoiceEntity? {
        return transaction {
            val result = InvoiceEntity.findById(id)
            if (eagerLoadActivities) result
            else result
        }
    }
}