package repository

import kotlinx.datetime.Clock
import models.invoicepdf.InvoicePdfModel
import models.invoicepdf.InvoicePdfStatus
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.updateReturning
import repository.entities.legacy.InvoicePdfEntity
import repository.entities.legacy.InvoicePdfStatusEntity
import repository.entities.legacy.InvoicePdfTable
import repository.mapper.toModel
import java.util.*

interface InvoicePdfRepository {
    suspend fun createInvoicePdf(
        invoiceId: UUID
    )

    suspend fun updateInvoicePdfState(
        invoiceId: UUID,
        status: InvoicePdfStatus,
        filePath: String,
    ): InvoicePdfModel

    suspend fun getInvoicePdf(
        invoiceId: UUID
    ): InvoicePdfModel?
}

internal class InvoicePdfRepositoryImpl(
    private val clock: Clock
) : InvoicePdfRepository {

    override suspend fun createInvoicePdf(invoiceId: UUID) {
        return newSuspendedTransaction {
            InvoicePdfTable.insert {
                it[invoice] = invoiceId
                it[createdAt] = clock.now()
                it[updatedAt] = clock.now()
                it[filePath] = ""
                it[status] = InvoicePdfStatusEntity.pending
            }
        }
    }

    override suspend fun updateInvoicePdfState(
        invoiceId: UUID,
        status: InvoicePdfStatus,
        filePath: String
    ): InvoicePdfModel {
        return newSuspendedTransaction {
            InvoicePdfTable.updateReturning(
                where = {
                    InvoicePdfTable.invoice eq invoiceId
                }
            ) { table ->
                table[updatedAt] = clock.now()
                table[InvoicePdfTable.status] = InvoicePdfStatusEntity.fromModel(status)
                table[InvoicePdfTable.filePath] = filePath
            }.map {
                InvoicePdfEntity.wrapRow(it)
            }.first().toModel()
        }
    }

    override suspend fun getInvoicePdf(invoiceId: UUID): InvoicePdfModel? {
        return newSuspendedTransaction {
            InvoicePdfEntity.find {
                InvoicePdfTable.invoice eq invoiceId
            }.firstOrNull()?.let {
                InvoicePdfModel(
                    id = it.id.value,
                    invoiceId = it.invoice.id.value,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                    path = it.filePath,
                    status = when (it.status) {
                        InvoicePdfStatusEntity.pending -> InvoicePdfStatus.Pending
                        InvoicePdfStatusEntity.success -> InvoicePdfStatus.Success
                        InvoicePdfStatusEntity.error -> InvoicePdfStatus.Failed
                    }
                )
            }
        }
    }
}