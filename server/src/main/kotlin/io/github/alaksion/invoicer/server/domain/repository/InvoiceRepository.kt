package io.github.alaksion.invoicer.server.domain.repository

import io.github.alaksion.invoicer.server.domain.model.CreateInvoiceModel
import io.github.alaksion.invoicer.server.domain.model.InvoiceModel
import io.github.alaksion.invoicer.server.domain.model.getinvoices.GetInvoicesFilterModel
import io.github.alaksion.invoicer.server.domain.model.getinvoices.InvoiceListItemModel
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
        limit: Int
    ): List<InvoiceListItemModel>

    suspend fun deleteByUUID(
        id: UUID
    )
}