package services.api.fakes.invoice

import models.InvoiceModel
import models.fixtures.invoiceModelFixture
import services.api.services.invoice.GetUserInvoiceByIdService
import java.util.*

class FakeGetUserInvoiceByIdService : GetUserInvoiceByIdService {

    var response: suspend () -> InvoiceModel = { invoiceModelFixture }

    override suspend fun get(invoiceId: UUID, userId: UUID): InvoiceModel {
        return response()
    }
}