package datasource.api.database

import entities.InvoiceActivityTable
import entities.InvoiceEntity
import entities.InvoiceListEntity
import entities.InvoiceTable
import models.createinvoice.CreateInvoiceModel
import models.getinvoices.GetInvoicesFilterModel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import utils.date.impl.DateProvider
import java.util.*

interface InvoiceDatabaseSource {
    suspend fun createInvoice(
        data: CreateInvoiceModel,
        userId: UUID,
    ): String

    suspend fun getInvoiceByUUID(
        id: UUID
    ): InvoiceEntity?

    suspend fun getInvoiceByExternalId(
        externalId: String
    ): InvoiceEntity?

    suspend fun getInvoices(
        filters: GetInvoicesFilterModel,
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
