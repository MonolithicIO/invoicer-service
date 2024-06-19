package invoicer.alaksiondev.com.entities

import invoicer.alaksiondev.com.database.PostgreEnum
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.date
import java.util.*

internal object InvoicePDFTable : UUIDTable("t_invoice_pdf") {
    val path = varchar("path", 100).nullable()
    val status = customEnumeration(
        name = "status",
        sql = "pdf_status",
        fromDb = { value -> InvoicePDFStatus.valueOf(value as String) },
        toDb = { PostgreEnum("pdf_status", it) })
    val invoice = reference("invoice_id", InvoiceTable)
    val createdAt = date("created_at")
    val updatedAt = date("updated_at")
}

internal class InvoicePDFEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<InvoicePDFEntity>(InvoicePDFTable)

    var path: String? by InvoicePDFTable.path
    var status: InvoicePDFStatus by InvoicePDFTable.status
    var createdAt: LocalDate by InvoicePDFTable.createdAt
    var updatedAt: LocalDate by InvoicePDFTable.updatedAt
    var invoice by InvoiceEntity referencedOn InvoicePDFTable.invoice
}

internal enum class InvoicePDFStatus {
    ok,
    pending,
    failed;
}