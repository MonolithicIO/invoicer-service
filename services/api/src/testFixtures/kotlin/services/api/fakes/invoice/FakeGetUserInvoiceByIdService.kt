package services.api.fakes.invoice

import models.fixtures.invoiceModelFixture
import models.invoice.InvoiceModel
import services.api.services.invoice.GetUserInvoiceByIdService
import java.util.*

class FakeGetUserInvoiceByIdService : GetUserInvoiceByIdService {

    var response: suspend () -> InvoiceModel = { invoiceModelFixture }

    override suspend fun get(invoiceId: UUID): InvoiceModel {
        return response()
    }
}