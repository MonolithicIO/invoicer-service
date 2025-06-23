package services.api.fakes.invoice

import models.InvoiceModelLegacy
import models.fixtures.invoiceModelLegacyFixture
import services.api.services.invoice.GetUserInvoiceByIdService
import java.util.*

class FakeGetUserInvoiceByIdService : GetUserInvoiceByIdService {

    var response: suspend () -> InvoiceModelLegacy = { invoiceModelLegacyFixture }

    override suspend fun get(invoiceId: UUID, userId: UUID): InvoiceModelLegacy {
        return response()
    }
}