package datasource.impl.database

import datasource.api.database.InvoicePdfDatabaseSource
import datasource.api.model.pdf.CreatePdfData
import datasource.impl.entities.InvoicePdfEntity
import datasource.impl.entities.InvoicePdfStatus
import datasource.impl.entities.InvoicePdfTable
import kotlinx.datetime.Clock
import models.invoicepdf.InvoicePdfModel
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

internal class InvoicePdfDatabaseSourceImpl(
    private val clock: Clock
) : InvoicePdfDatabaseSource {

    override suspend fun createPdf(payload: CreatePdfData) {
        newSuspendedTransaction {
            InvoicePdfTable.insert {
                it[invoice] = UUID.fromString(payload.invoiceId)
                it[createdAt] = clock.now()
                it[updatedAt] = clock.now()
                it[filePath] = payload.pdfPath
                it[status] = InvoicePdfStatus.PENDING
            }
        }
    }

    override suspend fun getInvoicePdf(invoiceId: String): InvoicePdfModel? {
        return newSuspendedTransaction {
            InvoicePdfEntity.find {
                InvoicePdfTable.invoice eq UUID.fromString(invoiceId)
            }.firstOrNull()?.let {
                InvoicePdfModel(
                    id = it.id.value.toString(),
                    invoiceId = it.invoice.id.value.toString(),
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                    path = it.filePath
                )
            }
        }
    }
}