package invoicer.alaksiondev.com.entities

import invoicer.alaksiondev.com.models.InvoicePdfModel
import invoicer.alaksiondev.com.models.PdfStatusModel
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.enum
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import java.time.LocalDate
import java.util.UUID

internal object InvoicePDFTable : Table<InvoicePDFEntity>("t_invoice_pdf") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val path = varchar("path").bindTo { it.path }
    val status = enum<InvoicePDFStatus>("status").bindTo { it.status }
    val invoiceId = uuid("invoice_id").references(InvoiceTable) {
        it.invoice
    }
    val createdAt = date("created_at").bindTo { it.createdAt }
    val updatedAt = date("updated_at").bindTo { it.updatedAt }
}

internal interface InvoicePDFEntity : Entity<InvoicePDFEntity> {
    companion object : Entity.Factory<InvoicePDFEntity>()

    val id: UUID
    var path: String
    var status: InvoicePDFStatus
    var invoice: InvoiceEntity
    var createdAt: LocalDate
    var updatedAt: LocalDate
}

internal fun InvoicePDFEntity.toDomain(): InvoicePdfModel = InvoicePdfModel(
    id = this.id.toString(),
    path = this.path,
    status = this.status.toDomain(),
    invoiceId = this.invoice.toString(),
    createdAt = this.createdAt.toString(),
    updatedAt = this.updatedAt.toString(),
)

internal enum class InvoicePDFStatus {
    ok,
    pending,
    failed;
}

internal fun InvoicePDFStatus.toDomain(): PdfStatusModel {
    return when (this) {
        InvoicePDFStatus.ok -> PdfStatusModel.Completed
        InvoicePDFStatus.failed -> PdfStatusModel.Failed
        InvoicePDFStatus.pending -> PdfStatusModel.Pending
    }
}

internal fun PdfStatusModel.fromDomain(): InvoicePDFStatus {
    return when (this) {
        PdfStatusModel.Completed -> InvoicePDFStatus.ok
        PdfStatusModel.Failed -> InvoicePDFStatus.failed
        PdfStatusModel.Pending -> InvoicePDFStatus.pending
    }
}