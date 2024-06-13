package invoicer.alaksiondev.com.entities

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.enum
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import java.time.LocalDate
import java.util.UUID

object InvoicePDFTable : Table<InvoicePDFEntity>("t_invoice_pdf") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val path = varchar("path").bindTo { it.path }
    val status = enum<InvoicePDFStatus>("status").bindTo { it.status }
    val invoiceId = uuid("invoice_id").references(InvoiceTable) {
        it.invoice
    }
    val createdAt = date("created_at").bindTo { it.createdAt }
    val updatedAt = date("updated_at").bindTo { it.updatedAt }
}

interface InvoicePDFEntity : Entity<InvoicePDFEntity> {
    companion object : Entity.Factory<InvoicePDFEntity>()

    val id: UUID
    var path: String
    var status: InvoicePDFStatus
    var invoice: InvoiceEntity
    var createdAt: LocalDate
    var updatedAt: LocalDate
}

enum class InvoicePDFStatus {
    ok,
    pending,
    failed;
}