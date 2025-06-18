package repository

import kotlinx.datetime.Clock
import models.customer.CreateCustomerModel
import models.customer.CustomerList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import repository.entities.CustomerEntity
import repository.entities.CustomerTable
import repository.mapper.toListItem
import java.util.*

interface CustomerRepository {
    suspend fun createCustomer(data: CreateCustomerModel): UUID

    suspend fun listCustomers(
        companyId: UUID,
        page: Long,
        limit: Int
    ): CustomerList
}

internal class CustomerRepositoryImpl(
    private val clock: Clock
) : CustomerRepository {

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

            val result = CustomerEntity.wrapRows(query)
                .toList()
                .map { it.toListItem() }

            CustomerList(
                items = result,
                nextPage = nextPage,
                itemCount = count
            )
        }
    }
}