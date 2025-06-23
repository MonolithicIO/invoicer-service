package services.impl.customer

import models.customer.CustomerModel
import repository.CustomerRepository
import services.api.services.customer.GetCustomerByIdService
import java.util.*

internal class GetCustomerByIdServiceImpl(
    private val customerRepository: CustomerRepository
) : GetCustomerByIdService {
    override suspend fun get(customerId: UUID): CustomerModel? {
        return customerRepository.getById(customerId)
    }
}