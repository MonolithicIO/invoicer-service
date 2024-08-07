package io.github.alaksion.invoicer.server.data.datasource

import io.github.alaksion.invoicer.server.data.entities.InvoiceActivityTable
import io.github.alaksion.invoicer.server.data.entities.InvoiceEntity
import io.github.alaksion.invoicer.server.data.entities.InvoiceTable
import io.github.alaksion.invoicer.server.domain.model.CreateInvoiceModel
import io.github.alaksion.invoicer.server.domain.model.getinvoices.GetInvoicesFilterModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import utils.date.api.DateProvider
import java.util.*

internal interface InvoiceDataSource {
    fun createInvoice(
        data: CreateInvoiceModel,
        userId: UUID
    ): String

    fun getInvoiceByUUID(
        uuid: UUID
    ): InvoiceEntity?

    fun getInvoiceByExternalId(
        externalId: String
    ): InvoiceEntity?

    fun getInvoicesFiltered(
        page: Long,
        limit: Int,
        filters: GetInvoicesFilterModel
    ): List<InvoiceEntity>

    fun deleteInvoice(
        id: UUID
    )
}

internal class InvoiceDataSourceImpl(
    private val dateProvider: DateProvider
) : InvoiceDataSource {

    override fun createInvoice(
        data: CreateInvoiceModel,
        userId: UUID
    ): String {
        val newInvoice = InvoiceTable.insertAndGetId {
            it[externalId] = data.externalId
            it[externalId] = data.externalId
            it[senderCompanyName] = data.senderCompanyName
            it[senderCompanyAddress] = data.senderCompanyName
            it[recipientCompanyName] = data.recipientCompanyName
            it[recipientCompanyAddress] = data.recipientCompanyAddress
            it[issueDate] = data.issueDate
            it[dueDate] = data.dueDate
            it[beneficiaryName] = data.beneficiaryName
            it[beneficiaryIban] = data.beneficiaryIban
            it[beneficiarySwift] = data.beneficiarySwift
            it[beneficiaryBankName] = data.beneficiaryBankName
            it[beneficiaryBankAddress] = data.beneficiaryBankAddress

            it[intermediaryIban] = data.intermediaryIban
            it[intermediarySwift] = data.intermediarySwift
            it[intermediaryBankName] = data.intermediaryBankName
            it[intermediaryBankAddress] = data.intermediaryBankAddress
            it[createdAt] = dateProvider.now()
            it[updatedAt] = dateProvider.now()
            it[user] = userId
        }
        val createdInvoiceId = newInvoice.value

        InvoiceActivityTable.batchInsert(data.activities) { item ->
            this[InvoiceActivityTable.invoice] = createdInvoiceId
            this[InvoiceActivityTable.quantity] = item.quantity
            this[InvoiceActivityTable.unitPrice] = item.unitPrice
            this[InvoiceActivityTable.description] = item.description
            this[InvoiceActivityTable.createdAt] = dateProvider.now()
            this[InvoiceActivityTable.updatedAt] = dateProvider.now()
        }

        return createdInvoiceId.toString()
    }

    override fun getInvoiceByUUID(uuid: UUID): InvoiceEntity? {
        return InvoiceEntity.findById(uuid)
    }

    override fun getInvoiceByExternalId(externalId: String): InvoiceEntity? {
        return InvoiceEntity.find {
            InvoiceTable.externalId eq externalId
        }.firstOrNull()
    }

    override fun getInvoicesFiltered(page: Long, limit: Int, filters: GetInvoicesFilterModel): List<InvoiceEntity> {
        val query = InvoiceTable
            .selectAll()

        filters.senderCompanyName?.let { senderCompany ->
            if (senderCompany.isNotBlank()) query.andWhere {
                InvoiceTable.senderCompanyName like "%$senderCompany%"
            }

        }

        filters.recipientCompanyName?.let { recipientCompany ->
            if (recipientCompany.isNotBlank()) query.andWhere {
                InvoiceTable.recipientCompanyName like "%$recipientCompany%"
            }
        }

        if (filters.maxDueDate != null && filters.minDueDate != null) query.andWhere {
            InvoiceTable.issueDate.between(filters.minDueDate, filters.maxDueDate)
        }

        if (filters.maxIssueDate != null && filters.minIssueDate != null) query.andWhere {
            InvoiceTable.issueDate.between(filters.minIssueDate, filters.maxIssueDate)
        }

        return InvoiceEntity.wrapRows(
            query.limit(n = limit, offset = page * limit)
        ).toList()
    }

    override fun deleteInvoice(id: UUID) {
        InvoiceTable.deleteWhere {
            InvoiceTable.id eq id
        }
    }
}