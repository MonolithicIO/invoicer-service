package io.github.alaksion.invoicer.server.repository

import io.github.alaksion.invoicer.server.entities.InvoicePDFEntity
import io.github.alaksion.invoicer.server.entities.InvoicePDFStatus
import io.github.alaksion.invoicer.server.entities.InvoicePDFTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

internal interface InvoicePdfRepository {
    suspend fun generateInvoicePdf(invoiceId: String): String

    suspend fun updateInvoicePdf(
        path: String?,
        status: InvoicePDFStatus,
        pdfId: String,
    ): InvoicePDFEntity?

    suspend fun deleteInvoicePdf(pdfId: UUID)

    suspend fun findPdfByInvoiceId(invoiceId: UUID): InvoicePDFEntity?
}

internal class InvoicePdfRepositoryImpl : InvoicePdfRepository {
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
    ): InvoicePDFEntity? {
        return transaction {
            InvoicePDFEntity.findByIdAndUpdate(id = UUID.fromString(pdfId)) { entity ->
                entity.path = path
                entity.status = status
            }
        }
    }

    override suspend fun deleteInvoicePdf(pdfId: UUID) {
        transaction {
            InvoicePDFTable.deleteWhere {
                InvoicePDFTable.id eq pdfId
            }
        }
    }

    override suspend fun findPdfByInvoiceId(invoiceId: UUID): InvoicePDFEntity? {
        return transaction {
            InvoicePDFEntity.find {
                InvoicePDFTable.invoice eq invoiceId
            }.firstOrNull()
        }
    }
}