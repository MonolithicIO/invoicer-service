package services.api.services.customer

import models.customer.CustomerList
import java.util.*

interface ListCustomersService {
    suspend fun list(
        page: Long,
        limit: Int,
        userId: UUID,
        companyId: UUID
    ): CustomerList
}