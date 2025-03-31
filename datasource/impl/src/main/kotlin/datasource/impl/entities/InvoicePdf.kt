package datasource.impl.entities

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

internal object InvoicePdfTable : UUIDTable("t_invoice_pdf") {
    val filePath = varchar("file_path", Int.MAX_VALUE)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val invoice = reference(name = "invoice_id", foreign = InvoiceTable, onDelete = ReferenceOption.CASCADE)
    val status = enumeration<InvoicePdfStatus>("status")
}

internal class InvoicePdfEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<InvoicePdfEntity>(InvoicePdfTable)

    var filePath by InvoicePdfTable.filePath
    var createdAt by InvoicePdfTable.createdAt
    var updatedAt by InvoicePdfTable.updatedAt
    var status by InvoicePdfTable.status
    val invoice by InvoiceEntity referencedOn InvoicePdfTable.invoice
}

internal enum class InvoicePdfStatus {
    PENDING,
    ERROR,
    SUCCESS
}