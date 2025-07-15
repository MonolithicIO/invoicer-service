package io.github.monolithic.invoicer.repository.fakes

import io.github.monolithic.invoicer.models.customer.CreateCustomerModel
import io.github.monolithic.invoicer.models.customer.CustomerList
import io.github.monolithic.invoicer.models.customer.CustomerModel
import io.github.monolithic.invoicer.repository.CustomerRepository
import java.util.*

class FakeCustomerRepository : CustomerRepository {

    var createCustomerResponse: () -> UUID = { UUID.fromString("123e4567-e89b-12d3-a456-426614174000") }
    var listCustomersResponse: () -> CustomerList = {
        CustomerList(
            items = listOf(),
            itemCount = 0,
            nextPage = null
        )
    }
    var findByCompanyIdAndEmailResponse: () -> CustomerModel? = { null }
    var findByIdResponse: () -> CustomerModel? = { null }

    override suspend fun createCustomer(data: CreateCustomerModel): UUID {
        return createCustomerResponse()
    }

    override suspend fun listCustomers(
        companyId: UUID,
        page: Long,
        limit: Int
    ): CustomerList {
        return listCustomersResponse()
    }

    override suspend fun findByCompanyIdAndEmail(
        companyId: UUID,
        email: String
    ): CustomerModel? {
        return findByCompanyIdAndEmailResponse()
    }

    override suspend fun getById(customerId: UUID): CustomerModel? {
        return findByIdResponse()
    }
}