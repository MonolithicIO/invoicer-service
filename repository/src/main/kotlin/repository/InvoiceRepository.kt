package repository

import foundation.cache.CacheHandler
import kotlinx.datetime.Clock
import models.getinvoices.GetInvoicesFilterModel
import models.invoice.CreateInvoiceModel
import models.invoice.InvoiceListModel
import models.invoice.InvoiceModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import repository.entities.InvoiceEntity
import repository.entities.InvoiceTable
import repository.entities.legacy.InvoiceActivityTable
import repository.entities.legacy.InvoiceTableLegacy
import repository.mapper.toListItemModel
import repository.mapper.toModel
import java.util.*

interface InvoiceRepository {
    suspend fun create(
        data: CreateInvoiceModel,
        userId: UUID,
    ): String

    suspend fun getById(
        id: UUID
    ): InvoiceModel?

    suspend fun getByInvoiceNumber(
        invoiceNumber: String
    ): InvoiceModel?

    suspend fun getAll(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        companyId: UUID,
    ): InvoiceListModel

    suspend fun delete(
        id: UUID
    )
}


internal class InvoiceRepositoryImpl(
    private val clock: Clock,
    private val cacheHandler: CacheHandler
) : InvoiceRepository {

    override suspend fun create(
        data: CreateInvoiceModel,
        userId: UUID,
    ): String {
        return newSuspendedTransaction {
            val newInvoice = InvoiceTable.insertAndGetId {
                // Company
                it[companyName] = data.company.name
                it[companyDocument] = data.company.document
                it[company] = data.company.id
                it[companyAddressLine1] = data.company.address.addressLine1
                it[companyAddressLine2] = data.company.address.addressLine2
                it[companyState] = data.company.address.state
                it[companyCity] = data.company.address.city
                it[companyZipCode] = data.company.address.postalCode
                it[companyCountryCode] = data.company.address.countryCode
                // Customer
                it[customer] = data.customer.id
                it[customerName] = data.customer.name
                // Invoice
                it[invoicerNumber] = data.invoicerNumber
                it[issueDate] = data.issueDate
                it[dueDate] = data.dueDate
                // General
                it[createdAt] = clock.now()
                it[updatedAt] = clock.now()
                it[isDeleted] = false
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

    override suspend fun getById(id: UUID): InvoiceModel? {
        val cached = cacheHandler.get(
            key = id.toString(),
            serializer = InvoiceModel.serializer()
        )

        if (cached != null) return cached

        return newSuspendedTransaction {
            InvoiceEntity.find {
                (InvoiceTable.id eq id) and (InvoiceTable.isDeleted eq false)
            }.firstOrNull()?.toModel()
        }?.also {
            cacheHandler.set(
                key = it.id.toString(),
                serializer = InvoiceModel.serializer(),
                value = it,
                ttlSeconds = CACHE_TTL_SECONDS
            )
        }
    }

    override suspend fun getByInvoiceNumber(invoiceNumber: String): InvoiceModel? {
        return newSuspendedTransaction {
            InvoiceEntity.find {
                (InvoiceTableLegacy.externalId eq invoiceNumber) and (InvoiceTableLegacy.isDeleted eq false)
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getAll(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        companyId: UUID,
    ): InvoiceListModel {
        return newSuspendedTransaction {
            val query = InvoiceTable
                .selectAll()
                .where {
                    InvoiceTable.company eq companyId and (InvoiceTable.isDeleted eq false)
                }

            if (filters.maxDueDate != null && filters.minDueDate != null) query.andWhere {
                InvoiceTable.issueDate.between(filters.minDueDate, filters.maxDueDate)
            }

            if (filters.maxIssueDate != null && filters.minIssueDate != null) query.andWhere {
                InvoiceTable.issueDate.between(filters.minIssueDate, filters.maxIssueDate)
            }

            if (filters.customerId != null) query.andWhere {
                InvoiceTable.customer eq filters.customerId
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
                totalCount = count
            )
        }
    }

    override suspend fun delete(
        id: UUID
    ) {
        newSuspendedTransaction {
            InvoiceTableLegacy.update(
                where = {
                    InvoiceTableLegacy.id eq id
                }
            ) {
                it[isDeleted] = true
                it[updatedAt] = clock.now()
            }
        }.also {
            cacheHandler.delete(
                key = id.toString(),
            )
        }
    }

    companion object {
        const val CACHE_TTL_SECONDS = 600L
    }
}