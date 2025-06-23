package services.api.services.customer

import models.customer.CustomerModel
import java.util.*

interface GetCustomerByIdService {
    suspend fun get(
        customerId: UUID,
    ): CustomerModel?
}