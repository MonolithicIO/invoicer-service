package io.github.alaksion.invoicer.server.data.datasource

import io.github.alaksion.invoicer.server.data.entities.InvoiceActivityTable
import io.github.alaksion.invoicer.server.data.entities.InvoiceEntity
import io.github.alaksion.invoicer.server.data.entities.InvoiceTable
import io.github.alaksion.invoicer.server.domain.model.CreateInvoiceModel
import io.github.alaksion.invoicer.server.domain.model.getinvoices.GetInvoicesFilterModel
import io.github.alaksion.invoicer.server.util.DateProvider
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll
import java.util.*

internal interface InvoiceDataSource {
    fun createInvoice(
        data: CreateInvoiceModel
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
}

internal class InvoiceDataSourceImpl(
    private val dateProvider: DateProvider
) : InvoiceDataSource {

    override fun createInvoice(data: CreateInvoiceModel): String {
        val newInvoice = InvoiceEntity.new {
            externalId = data.externalId
            externalId = data.externalId
            senderCompanyName = data.senderCompanyName
            senderCompanyAddress = data.senderCompanyName
            recipientCompanyName = data.recipientCompanyName
            recipientCompanyAddress = data.recipientCompanyAddress
            issueDate = data.issueDate
            dueDate = data.dueDate
            beneficiaryName = data.beneficiaryName
            beneficiaryIban = data.beneficiaryIban
            beneficiarySwift = data.beneficiarySwift
            beneficiaryBankName = data.beneficiaryBankName
            beneficiaryBankAddress = data.beneficiaryBankAddress

            intermediaryIban = data.intermediaryIban
            intermediarySwift = data.intermediarySwift
            intermediaryBankName = data.intermediaryBankName
            intermediaryBankAddress = data.intermediaryBankAddress
            createdAt = dateProvider.now()
            updatedAt = dateProvider.now()
        }
        val createdInvoiceId = newInvoice.id.value

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
            .limit(n = limit, offset = page * limit)

        return InvoiceEntity.wrapRows(query).toList()
    }
}