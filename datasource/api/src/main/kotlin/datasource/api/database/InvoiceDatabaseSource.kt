package datasource.api.database

import datasource.api.model.invoice.CreateInvoiceData
import datasource.api.model.invoice.GetInvoicesFilterData
import models.InvoiceModel
import models.getinvoices.InvoiceListItemModel
import models.getinvoices.InvoiceListModel
import java.util.*

interface InvoiceDatabaseSource {
    suspend fun createInvoice(
        data: CreateInvoiceData,
        userId: UUID,
    ): String

    suspend fun getInvoiceByUUID(
        id: UUID
    ): InvoiceModel?

    suspend fun getInvoiceByExternalId(
        externalId: String
    ): InvoiceModel?

    suspend fun getInvoices(
        filters: GetInvoicesFilterData,
        page: Long,
        limit: Int,
        userId: String,
    ): InvoiceListModel

    suspend fun delete(
        id: UUID
    )

    suspend fun getInvoicesByBeneficiaryId(
        beneficiaryId: UUID,
        userId: UUID,
    ): List<InvoiceListItemModel>

    suspend fun getInvoicesByIntermediaryId(
        intermediaryId: UUID,
        userId: UUID,
    ): List<InvoiceListItemModel>
}
