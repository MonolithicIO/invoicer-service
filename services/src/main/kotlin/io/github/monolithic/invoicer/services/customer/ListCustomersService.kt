package io.github.monolithic.invoicer.services.customer

import io.github.monolithic.invoicer.models.customer.CustomerList
import java.util.*
import io.github.monolithic.invoicer.repository.CustomerRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService

interface ListCustomersService {
    suspend fun list(
        page: Long,
        limit: Int,
        userId: UUID,
        companyId: UUID
    ): CustomerList
}

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
