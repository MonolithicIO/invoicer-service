package services.api.services.customer

import models.customer.CreateCustomerModel
import java.util.*

interface CreateCustomerService {
    suspend fun createCustomer(
        userId: UUID,
        data: CreateCustomerModel
    ): String
}
