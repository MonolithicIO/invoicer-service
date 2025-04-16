package datasource.api.database

import datasource.api.model.pdf.CreatePdfData
import models.fixtures.invoicePdfModelFixture
import models.invoicepdf.InvoicePdfModel
import models.invoicepdf.InvoicePdfStatus
import java.util.*

class FakeInvoicePdfDatabaseSource : InvoicePdfDatabaseSource {

    var getInvoicePdfResponse: suspend () -> InvoicePdfModel? = { null }
    var updateInvoicePdfResponse: suspend () -> InvoicePdfModel = { invoicePdfModelFixture }

    val createPdfCallStack = mutableListOf<CreatePdfData>()
    val getInvoicePdfCallStack = mutableListOf<UUID>()

    override suspend fun createPdf(payload: CreatePdfData) {
        createPdfCallStack.add(payload)
    }

    override suspend fun getInvoicePdf(invoiceId: UUID): InvoicePdfModel? {
        getInvoicePdfCallStack.add(invoiceId)
        return getInvoicePdfResponse()
    }

    override suspend fun updateInvoicePdfState(
        invoiceId: UUID,
        status: InvoicePdfStatus,
        filePath: String
    ): InvoicePdfModel {
        return updateInvoicePdfResponse()
    }
}