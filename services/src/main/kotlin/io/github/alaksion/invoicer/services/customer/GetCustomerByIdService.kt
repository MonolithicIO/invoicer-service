package io.github.alaksion.invoicer.services.customer

import models.customer.CustomerModel
import java.util.*
import repository.CustomerRepository

interface GetCustomerByIdService {
    suspend fun get(
        customerId: UUID,
    ): CustomerModel?
}

internal class GetCustomerByIdServiceImpl(
    private val customerRepository: CustomerRepository
) : GetCustomerByIdService {
    override suspend fun get(customerId: UUID): CustomerModel? {
        return customerRepository.getById(customerId)
    }
}
