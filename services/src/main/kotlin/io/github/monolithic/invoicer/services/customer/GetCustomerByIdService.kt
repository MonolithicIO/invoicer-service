package io.github.monolithic.invoicer.services.customer

import io.github.monolithic.invoicer.models.customer.CustomerModel
import java.util.*
import io.github.monolithic.invoicer.repository.CustomerRepository

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
