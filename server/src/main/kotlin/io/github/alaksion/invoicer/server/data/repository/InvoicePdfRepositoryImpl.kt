package io.github.alaksion.invoicer.server.data.repository

import io.github.alaksion.invoicer.server.data.datasource.InvoiceDataSource
import io.github.alaksion.invoicer.server.data.entities.InvoicePDFEntity
import io.github.alaksion.invoicer.server.data.entities.InvoicePDFStatus
import io.github.alaksion.invoicer.server.data.entities.InvoicePDFTable
import io.github.alaksion.invoicer.server.data.entities.toModel
import io.github.alaksion.invoicer.server.domain.model.pdf.InvoicePdfModel
import io.github.alaksion.invoicer.server.domain.repository.InvoicePdfRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

internal class InvoicePdfRepositoryImpl(
    private val dataSource: InvoiceDataSource
) : InvoicePdfRepository {

    override suspend fun generateInvoicePdf(invoiceId: String): String {
        return transaction {
            InvoicePDFTable.insertAndGetId {
                it[path] = null
                it[invoice] = UUID.fromString(invoiceId)
                it[status] = InvoicePDFStatus.pending
            }
        }.value.toString()
    }

    override suspend fun updateInvoicePdf(
        path: String?,
        status: InvoicePDFStatus,
        pdfId: String
    ): InvoicePdfModel? {
        return transaction {
            InvoicePDFEntity.findByIdAndUpdate(id = UUID.fromString(pdfId)) { entity ->
                entity.path = path
                entity.status = status
            }?.toModel()
        }
    }

    override suspend fun deleteInvoicePdf(pdfId: UUID) {
        transaction {
            InvoicePDFTable.deleteWhere {
                InvoicePDFTable.id eq pdfId
            }
        }
    }

    override suspend fun findPdfByInvoiceId(invoiceId: UUID): InvoicePdfModel? {
        return transaction {
            InvoicePDFEntity.find {
                InvoicePDFTable.invoice eq invoiceId
            }.firstOrNull()?.toModel()
        }
    }

}

