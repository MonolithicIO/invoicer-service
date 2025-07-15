package io.github.monolithic.invoicer.repository

import io.github.monolithic.invoicer.models.customer.CreateCustomerModel
import io.github.monolithic.invoicer.models.customer.CustomerList
import io.github.monolithic.invoicer.models.customer.CustomerModel
import io.github.monolithic.invoicer.repository.datasource.CustomerDataSource
import java.util.*

interface CustomerRepository {
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

internal class CustomerRepositoryImpl(
    private val customerDataSource: CustomerDataSource
) : CustomerRepository {

    override suspend fun createCustomer(data: CreateCustomerModel): UUID {
        return customerDataSource.createCustomer(data)
    }

    override suspend fun listCustomers(
        companyId: UUID,
        page: Long,
        limit: Int
    ): CustomerList {
        return customerDataSource.listCustomers(
            companyId = companyId,
            page = page,
            limit = limit
        )
    }

    override suspend fun findByCompanyIdAndEmail(
        companyId: UUID,
        email: String
    ): CustomerModel? {
        return customerDataSource.findByCompanyIdAndEmail(
            companyId = companyId,
            email = email
        )
    }

    override suspend fun getById(customerId: UUID): CustomerModel? {
        return customerDataSource.getById(
            customerId = customerId
        )
    }
}
