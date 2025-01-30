package repository.test.repository

import models.InvoiceModel
import models.createinvoice.CreateInvoiceModel
import models.getinvoices.GetInvoicesFilterModel
import models.getinvoices.InvoiceListItemModel
import models.getinvoices.InvoiceListModel
import repository.api.repository.InvoiceRepository
import java.util.*

class FakeInvoiceRepository : InvoiceRepository {

    var getInvoicesByBeneficiaryIdResponse: () -> List<InvoiceListItemModel> = { listOf() }
    var getInvoicesByIntermediaryIdResponse: () -> List<InvoiceListItemModel> = { listOf() }
    var getInvoiceByExternalIdResponse: () -> InvoiceModel? = { null }

    override suspend fun createInvoice(data: CreateInvoiceModel, userId: UUID): String {
        TODO("Not yet implemented")
    }

    override suspend fun getInvoiceByUUID(id: UUID): InvoiceModel? {
        TODO("Not yet implemented")
    }

    override suspend fun getInvoiceByExternalId(externalId: String): InvoiceModel? {
        return getInvoiceByExternalIdResponse()
    }

    override suspend fun getInvoices(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: String
    ): InvoiceListModel {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: UUID) {
        TODO("Not yet implemented")
    }

    override suspend fun getInvoicesByBeneficiaryId(beneficiaryId: UUID, userId: UUID): List<InvoiceListItemModel> {
        return getInvoicesByBeneficiaryIdResponse()
    }

    override suspend fun getInvoicesByIntermediaryId(intermediaryId: UUID, userId: UUID): List<InvoiceListItemModel> {
        return getInvoicesByIntermediaryIdResponse()
    }
}