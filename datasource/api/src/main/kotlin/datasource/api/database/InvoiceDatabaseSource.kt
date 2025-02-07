package datasource.api.database

import datasource.api.model.invoice.CreateInvoiceData
import datasource.api.model.invoice.GetInvoicesFilterData
import entities.InvoiceEntity
import entities.InvoiceListEntity
import java.util.*

interface InvoiceDatabaseSource {
    suspend fun createInvoice(
        data: CreateInvoiceData,
        userId: UUID,
    ): String

    suspend fun getInvoiceByUUID(
        id: UUID
    ): InvoiceEntity?

    suspend fun getInvoiceByExternalId(
        externalId: String
    ): InvoiceEntity?

    suspend fun getInvoices(
        filters: GetInvoicesFilterData,
        page: Long,
        limit: Int,
        userId: String,
    ): InvoiceListEntity

    suspend fun delete(
        id: UUID
    )

    suspend fun getInvoicesByBeneficiaryId(
        beneficiaryId: UUID,
        userId: UUID,
    ): List<InvoiceEntity>

    suspend fun getInvoicesByIntermediaryId(
        intermediaryId: UUID,
        userId: UUID,
    ): List<InvoiceEntity>
}
