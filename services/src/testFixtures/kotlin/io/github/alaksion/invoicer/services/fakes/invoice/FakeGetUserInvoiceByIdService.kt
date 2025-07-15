package io.github.alaksion.invoicer.services.fakes.invoice

import java.util.*
import models.fixtures.invoiceModelFixture
import models.invoice.InvoiceModel
import io.github.alaksion.invoicer.services.invoice.GetUserInvoiceByIdService

class FakeGetUserInvoiceByIdService : GetUserInvoiceByIdService {

    var response: suspend () -> InvoiceModel = { invoiceModelFixture }

    override suspend fun get(
        invoiceId: UUID,
        companyId: UUID,
        userId: UUID
    ): InvoiceModel {
        return response()
    }
}