package io.github.monolithic.invoicer.services.fakes.invoice

import java.util.*
import io.github.monolithic.invoicer.models.fixtures.invoiceModelFixture
import io.github.monolithic.invoicer.models.invoice.InvoiceModel
import io.github.monolithic.invoicer.services.invoice.GetUserInvoiceByIdService

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