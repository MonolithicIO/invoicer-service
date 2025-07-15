package io.github.alaksion.invoicer.services.customer

import models.customer.CustomerList
import java.util.*
import repository.CustomerRepository
import io.github.alaksion.invoicer.services.user.GetUserByIdService

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
