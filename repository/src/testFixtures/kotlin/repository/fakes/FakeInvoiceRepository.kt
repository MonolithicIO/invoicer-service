package repository.fakes

import models.InvoiceModelLegacy
import models.invoice.CreateInvoiceModel
import models.fixtures.invoiceListItemModelLegacyFixture
import models.fixtures.invoiceListModelLegacyFixture
import models.fixtures.invoiceModelLegacyFixture
import models.getinvoices.GetInvoicesFilterModel
import models.getinvoices.InvoiceListItemModelLegacy
import models.getinvoices.InvoiceListModelLegacy
import repository.InvoiceRepository
import java.util.*

class FakeInvoiceRepository : InvoiceRepository {

    var getInvoicesByBeneficiaryIdResponse: () -> List<InvoiceListItemModelLegacy> = { listOf(invoiceListItemModelLegacyFixture) }
    var getInvoicesByIntermediaryIdResponse: () -> List<InvoiceListItemModelLegacy> = { listOf(invoiceListItemModelLegacyFixture) }
    var getInvoiceByExternalIdResponse: () -> InvoiceModelLegacy? = { invoiceModelLegacyFixture }
    var getInvoiceByIdResponse: () -> InvoiceModelLegacy? = { invoiceModelLegacyFixture }
    var getInvoicesResponse: () -> InvoiceListModelLegacy = { invoiceListModelLegacyFixture }
    var createInvoiceResponse: suspend () -> String = { DEFAULT_CREATE_RESPONSE }
    var deleteResponse: suspend () -> Unit = {}

    val deleteCallStack = mutableListOf<UUID>()
    val createCallStack = mutableListOf<String>()

    override suspend fun create(data: CreateInvoiceModel, userId: UUID): String {
        val response = createInvoiceResponse()
        createCallStack.add(response)
        return response
    }

    override suspend fun getById(id: UUID): InvoiceModelLegacy? {
        return getInvoiceByIdResponse()
    }

    override suspend fun getByInvoiceNumber(invoiceNumber: String): InvoiceModelLegacy? {
        return getInvoiceByExternalIdResponse()
    }

    override suspend fun getAll(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        companyId: UUID
    ): InvoiceListModelLegacy {
        return getInvoicesResponse()
    }

    override suspend fun delete(id: UUID) {
        deleteCallStack.add(id)
        return deleteResponse()
    }

    override suspend fun getInvoicesByBeneficiaryId(beneficiaryId: UUID, userId: UUID): List<InvoiceListItemModelLegacy> {
        return getInvoicesByBeneficiaryIdResponse()
    }

    override suspend fun getInvoicesByIntermediaryId(intermediaryId: UUID, userId: UUID): List<InvoiceListItemModelLegacy> {
        return getInvoicesByIntermediaryIdResponse()
    }

    companion object {
        val DEFAULT_CREATE_RESPONSE = "1234"
    }
}