package datasource.impl.entities

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

internal object InvoicePdfTable : UUIDTable("t_invoice_pdf") {
    val filePath = varchar("file_path", Int.MAX_VALUE)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val invoice = reference(name = "invoice_id", foreign = InvoiceTable, onDelete = ReferenceOption.CASCADE)
    val status = enumeration<InvoicePdfStatus>("status")
}

internal enum class InvoicePdfStatus {
    PENDING,
    ERROR,
    SUCCESS
}