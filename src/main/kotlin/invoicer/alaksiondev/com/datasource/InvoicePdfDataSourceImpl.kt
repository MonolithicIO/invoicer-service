package invoicer.alaksiondev.com.datasource

import invoicer.alaksiondev.com.entities.InvoicePDFStatus
import invoicer.alaksiondev.com.entities.InvoicePDFTable
import org.ktorm.database.Database
import org.ktorm.dsl.insertAndGenerateKey
import java.util.UUID

interface InvoicePdfDataSource {
    suspend fun generateInvoicePdf(invoiceId: String): String
}

class InvoicePdfDataSourceImpl(
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
}