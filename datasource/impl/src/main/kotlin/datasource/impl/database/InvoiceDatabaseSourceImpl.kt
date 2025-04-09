package datasource.impl.database

import datasource.api.database.InvoiceDatabaseSource
import datasource.api.model.invoice.CreateInvoiceData
import datasource.api.model.invoice.GetInvoicesFilterData
import datasource.impl.entities.InvoiceActivityTable
import datasource.impl.entities.InvoiceEntity
import datasource.impl.entities.InvoiceTable
import datasource.impl.mapper.toListItemModel
import datasource.impl.mapper.toModel
import kotlinx.datetime.Clock
import models.InvoiceModel
import models.getinvoices.InvoiceListItemModel
import models.getinvoices.InvoiceListModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

internal class InvoiceDatabaseSourceImpl(
    private val clock: Clock
) : InvoiceDatabaseSource {

    override suspend fun createInvoice(
        data: CreateInvoiceData,
        userId: UUID,
    ): String {
        return newSuspendedTransaction {
            val newInvoice = InvoiceTable.insertAndGetId {
                it[externalId] = data.externalId
                it[externalId] = data.externalId
                it[senderCompanyName] = data.senderCompanyName
                it[senderCompanyAddress] = data.senderCompanyAddress
                it[recipientCompanyName] = data.recipientCompanyName
                it[recipientCompanyAddress] = data.recipientCompanyAddress
                it[issueDate] = data.issueDate
                it[dueDate] = data.dueDate
                it[beneficiary] = data.beneficiaryId
                it[intermediary] = data.intermediaryId
                it[createdAt] = clock.now()
                it[updatedAt] = clock.now()
                it[user] = userId
            }
            val createdInvoiceId = newInvoice.value

            InvoiceActivityTable.batchInsert(data.activities) { item ->
                this[InvoiceActivityTable.invoice] = createdInvoiceId
                this[InvoiceActivityTable.quantity] = item.quantity
                this[InvoiceActivityTable.unitPrice] = item.unitPrice
                this[InvoiceActivityTable.description] = item.description
                this[InvoiceActivityTable.createdAt] = clock.now()
                this[InvoiceActivityTable.updatedAt] = clock.now()
            }

            createdInvoiceId.toString()
        }
    }

    override suspend fun getInvoiceByUUID(id: UUID): InvoiceModel? {
        return newSuspendedTransaction {
            InvoiceEntity.find {
                (InvoiceTable.id eq id) and (InvoiceTable.isDeleted eq false)
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getInvoiceByExternalId(externalId: String): InvoiceModel? {
        return newSuspendedTransaction {
            InvoiceEntity.find {
                (InvoiceTable.externalId eq externalId) and (InvoiceTable.isDeleted eq false)
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getInvoices(
        filters: GetInvoicesFilterData,
        page: Long,
        limit: Int,
        userId: String,
    ): InvoiceListModel {
        return newSuspendedTransaction {
            val query = InvoiceTable
                .selectAll()
                .where {
                    InvoiceTable.user eq UUID.fromString(userId) and (InvoiceTable.isDeleted eq false)
                }

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

            val count = query.count()
            val currentOffset = page * limit

            val nextPage = if (count > currentOffset) {
                (count - currentOffset) / limit
            } else {
                null
            }

            val result = InvoiceEntity.wrapRows(query.limit(n = limit, offset = currentOffset))
                .toList().map { it.toListItemModel() }

            InvoiceListModel(
                items = result,
                nextPage = nextPage,
                totalResults = count
            )
        }
    }

    override suspend fun delete(
        id: UUID
    ) {
        newSuspendedTransaction {
            InvoiceTable.update(
                where = {
                    InvoiceTable.id eq id
                }
            ) {
                it[isDeleted] = true
                it[updatedAt] = clock.now()
            }
        }
    }

    override suspend fun getInvoicesByBeneficiaryId(
        beneficiaryId: UUID,
        userId: UUID
    ): List<InvoiceListItemModel> {
        return newSuspendedTransaction {
            InvoiceEntity.find {
                (InvoiceTable.beneficiary eq beneficiaryId) and (InvoiceTable.user eq userId) and (InvoiceTable.isDeleted eq false)
            }.toList().map { it.toListItemModel() }
        }
    }

    override suspend fun getInvoicesByIntermediaryId(
        intermediaryId: UUID,
        userId: UUID
    ): List<InvoiceListItemModel> {
        return newSuspendedTransaction {
            InvoiceEntity.find {
                (InvoiceTable.intermediary eq intermediaryId) and (InvoiceTable.user eq userId) and (InvoiceTable.isDeleted eq false)
            }.toList().map { it.toListItemModel() }
        }
    }

}