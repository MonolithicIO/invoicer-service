package invoicer.alaksiondev.com.datasource

import invoicer.alaksiondev.com.entities.InvoicePDFEntity
import invoicer.alaksiondev.com.entities.InvoicePDFStatus
import invoicer.alaksiondev.com.entities.InvoicePDFTable
import org.ktorm.database.Database
import org.ktorm.dsl.delete
import org.ktorm.dsl.eq
import org.ktorm.dsl.insertAndGenerateKey
import org.ktorm.dsl.update
import org.ktorm.entity.find
import org.ktorm.entity.sequenceOf
import java.time.LocalDate
import java.util.UUID

internal interface InvoicePdfDataSource {
    suspend fun generateInvoicePdf(invoiceId: String): String

    suspend fun updateInvoicePdf(
        path: String?,
        status: InvoicePDFStatus,
        pdfId: String,
    ): String

    suspend fun deleteInvoicePdf(pdfId: String): String

    suspend fun findPdfByInvoiceId(invoiceId: String): InvoicePDFEntity?
}

internal class InvoicePdfDataSourceImpl(
    private val database: Database
) : InvoicePdfDataSource {
    override suspend fun generateInvoicePdf(invoiceId: String): String {
        val response = database.insertAndGenerateKey(InvoicePDFTable) { newRecord ->
            set(newRecord.invoiceId, UUID.fromString(invoiceId))
            set(newRecord.path, null)
            set(newRecord.status, InvoicePDFStatus.pending)
        }

        return (response as UUID).toString()
    }

    override suspend fun updateInvoicePdf(
        path: String?,
        status: InvoicePDFStatus,
        pdfId: String
    ): String {
        database.update(InvoicePDFTable) { record ->
            set(record.path, path)
            set(record.status, status)
            set(record.updatedAt, LocalDate.now())
            where { record.id eq UUID.fromString(pdfId) }
        }
        return pdfId
    }

    override suspend fun deleteInvoicePdf(pdfId: String): String {
        database.delete(InvoicePDFTable) { record ->
            record.id eq record.invoiceId
        }

        return pdfId
    }

    override suspend fun findPdfByInvoiceId(invoiceId: String): InvoicePDFEntity? {
        return database.sequenceOf(InvoicePDFTable).find {
            it.invoiceId eq UUID.fromString(invoiceId)
        }
    }
}