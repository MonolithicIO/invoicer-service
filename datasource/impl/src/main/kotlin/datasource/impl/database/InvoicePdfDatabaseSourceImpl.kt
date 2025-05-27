package datasource.impl.database

import datasource.api.database.InvoicePdfDatabaseSource
import datasource.api.model.pdf.CreatePdfData
import datasource.impl.entities.legacy.InvoicePdfEntity
import datasource.impl.entities.legacy.InvoicePdfStatusEntity
import datasource.impl.entities.legacy.InvoicePdfTable
import datasource.impl.mapper.toModel
import kotlinx.datetime.Clock
import models.invoicepdf.InvoicePdfModel
import models.invoicepdf.InvoicePdfStatus
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.updateReturning
import java.util.*

internal class InvoicePdfDatabaseSourceImpl(
    private val clock: Clock
) : InvoicePdfDatabaseSource {

    override suspend fun createPdf(payload: CreatePdfData) {
        newSuspendedTransaction {
            InvoicePdfTable.insert {
                it[invoice] = payload.invoiceId
                it[createdAt] = clock.now()
                it[updatedAt] = clock.now()
                it[filePath] = payload.pdfPath
                it[status] = InvoicePdfStatusEntity.pending
            }
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
}