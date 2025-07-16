package io.github.monolithic.invoicer.repository.fakes

import io.github.monolithic.invoicer.models.fixtures.invoiceListFixture
import io.github.monolithic.invoicer.models.fixtures.invoiceModelFixture
import io.github.monolithic.invoicer.models.invoice.CreateInvoiceModel
import io.github.monolithic.invoicer.models.invoice.GetInvoicesFilterModel
import io.github.monolithic.invoicer.models.invoice.InvoiceListModel
import io.github.monolithic.invoicer.models.invoice.InvoiceModel
import io.github.monolithic.invoicer.repository.InvoiceRepository
import java.util.*

class FakeInvoiceRepository : InvoiceRepository {

    var getInvoiceByExternalIdResponse: () -> InvoiceModel? = { invoiceModelFixture }
    var getInvoiceByIdResponse: () -> InvoiceModel? = { invoiceModelFixture }
    var getInvoicesResponse: () -> InvoiceListModel = { invoiceListFixture }
    var createInvoiceResponse: suspend () -> String = { DEFAULT_CREATE_RESPONSE }
    var deleteResponse: suspend () -> Unit = {}

    val deleteCallStack = mutableListOf<UUID>()
    val createCallStack = mutableListOf<String>()

    override suspend fun create(data: CreateInvoiceModel, userId: UUID): String {
        val response = createInvoiceResponse()
        createCallStack.add(response)
        return response
    }

    override suspend fun getById(id: UUID): InvoiceModel? {
        return getInvoiceByIdResponse()
    }

    override suspend fun getByInvoiceNumber(invoiceNumber: String): InvoiceModel? {
        return getInvoiceByExternalIdResponse()
    }

    override suspend fun getAll(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        companyId: UUID
    ): InvoiceListModel {
        return getInvoicesResponse()
    }

    override suspend fun delete(id: UUID) {
        deleteCallStack.add(id)
        return deleteResponse()
    }

    companion object {
        val DEFAULT_CREATE_RESPONSE = "1234"
    }
}