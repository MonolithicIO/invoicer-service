package services.impl.customer

import models.customer.CustomerList
import repository.CustomerRepository
import services.api.services.customer.ListCustomersService
import services.api.services.user.GetUserByIdService
import java.util.*

internal class ListCustomerServiceImpl(
    private val customerRepository: CustomerRepository,
    private val getUserByIdService: GetUserByIdService
) : ListCustomersService {

    override suspend fun list(
        page: Long,
        limit: Int,
        userId: UUID,
        companyId: UUID
    ): CustomerList {
        getUserByIdService.get(userId)

        return customerRepository.listCustomers(
            page = page,
            limit = limit,
            companyId = companyId
        )
    }
}