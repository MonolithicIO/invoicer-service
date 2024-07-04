package io.github.alaksion.invoicer.server.repository

import io.github.alaksion.invoicer.server.data.entities.InvoiceEntity
import io.github.alaksion.invoicer.server.data.entities.InvoiceTable
import io.github.alaksion.invoicer.server.domain.model.GetInvoicesFilterModel
import io.github.alaksion.invoicer.server.util.DateProvider
import io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.CreateInvoiceViewModel
import org.jetbrains.exposed.dao.load
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

internal interface InvoiceRepository {
    suspend fun createInvoice(model: CreateInvoiceViewModel): String
    suspend fun getInvoiceByExternalId(externalId: String): InvoiceEntity?
    suspend fun getInvoiceById(
        id: UUID,
        eagerLoadActivities: Boolean
    ): InvoiceEntity?

    suspend fun getInvoices(
        filterModel: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
    ): List<InvoiceEntity>
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
            if (eagerLoadActivities) InvoiceEntity.findById(id)?.load(InvoiceEntity::activities)
            else InvoiceEntity.findById(id)
        }
    }

    override suspend fun getInvoices(
        filterModel: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
    ): List<InvoiceEntity> {
        return transaction {
            val query = InvoiceTable
                .selectAll()
                .limit(n = limit, offset = page * limit)

            InvoiceEntity.wrapRows(query).toList()
        }
    }
}