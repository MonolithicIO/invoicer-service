package services.api.fakes.customer

import kotlinx.datetime.Instant
import models.customer.CustomerModel
import services.api.services.customer.GetCustomerByIdService
import java.util.*

class FakeCustomerByIdService : GetCustomerByIdService {

    var response: CustomerModel? = CustomerModel(
        id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        name = "adwa",
        email = "teste@gmail.com",
        phone = "1231321",
        companyId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
        createdAt = Instant.parse("2023-01-01T00:00:00Z"),
        updatedAt = Instant.parse("2023-01-01T00:00:00Z")
    )

    override suspend fun get(customerId: UUID): CustomerModel? {
        return response
    }
}