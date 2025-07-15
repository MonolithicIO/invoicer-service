package io.github.monolithic.invoicer.repository.datasource

import io.github.monolithic.invoicer.repository.entities.CustomerEntity
import io.github.monolithic.invoicer.repository.entities.CustomerTable
import io.github.monolithic.invoicer.repository.mapper.toListItem
import io.github.monolithic.invoicer.repository.mapper.toModel
import kotlinx.datetime.Clock
import io.github.monolithic.invoicer.models.customer.CreateCustomerModel
import io.github.monolithic.invoicer.models.customer.CustomerList
import io.github.monolithic.invoicer.models.customer.CustomerModel
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

interface CustomerDataSource {
    suspend fun createCustomer(data: CreateCustomerModel): UUID

    suspend fun listCustomers(
        companyId: UUID,
        page: Long,
        limit: Int
    ): CustomerList

    suspend fun findByCompanyIdAndEmail(
        companyId: UUID,
        email: String
    ): CustomerModel?

    suspend fun getById(
        customerId: UUID
    ): CustomerModel?
}

internal class CustomerDataSourceImpl(
    private val clock: Clock
) : CustomerDataSource {

    override suspend fun createCustomer(data: CreateCustomerModel): UUID {
        return newSuspendedTransaction {
            CustomerTable.insertAndGetId {
                it[name] = data.name
                it[email] = data.email
                it[phone] = data.phone
                it[company] = data.companyId
                it[createdAt] = clock.now()
                it[updatedAt] = clock.now()
                it[isDeleted] = false
            }
        }.value
    }

    override suspend fun listCustomers(
        companyId: UUID,
        page: Long,
        limit: Int
    ): CustomerList {
        return newSuspendedTransaction {
            val query = CustomerTable
                .selectAll()
                .where {
                    CustomerTable.company eq companyId and (CustomerTable.isDeleted eq false)
                }
                .limit(n = limit, offset = page * limit)

            val count = query.count()
            val currentOffset = page * limit

            val nextPage = if (count > currentOffset) {
                (count - currentOffset) / limit
            } else {
                null
            }

            val result = CustomerEntity.Companion.wrapRows(query)
                .toList()
                .map { it.toListItem() }

            CustomerList(
                items = result,
                nextPage = nextPage,
                itemCount = count
            )
        }
    }

    override suspend fun findByCompanyIdAndEmail(
        companyId: UUID,
        email: String
    ): CustomerModel? {
        return newSuspendedTransaction {
            CustomerEntity.Companion.find {
                (CustomerTable.company eq companyId) and
                        (CustomerTable.email eq email)
            }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getById(customerId: UUID): CustomerModel? {
        return newSuspendedTransaction {
            CustomerEntity.Companion.find {
                (CustomerTable.id eq customerId)
            }.firstOrNull()?.toModel()
        }
    }
}
