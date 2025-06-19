package repository.fakes

import models.customer.CreateCustomerModel
import models.customer.CustomerList
import repository.CustomerRepository
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
}