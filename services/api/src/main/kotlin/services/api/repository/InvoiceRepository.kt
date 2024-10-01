package services.api.repository

import services.api.model.InvoiceModel
import services.api.model.createinvoice.CreateInvoiceModel
import services.api.model.getinvoices.GetInvoicesFilterModel
import services.api.model.getinvoices.InvoiceListItemModel
import java.util.*

interface InvoiceRepository {
    suspend fun createInvoice(
        data: CreateInvoiceModel,
        userId: UUID,
    ): String

    suspend fun getInvoiceByUUID(
        id: UUID
    ): InvoiceModel?

    suspend fun getInvoiceByExternalId(
        externalId: String
    ): InvoiceModel?

    suspend fun getInvoices(
        filters: GetInvoicesFilterModel,
        page: Long,
        limit: Int,
        userId: String,
    ): List<InvoiceListItemModel>

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