package repository.entities

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import java.util.*

internal object InvoiceActivityTable : UUIDTable("t_invoice_activity") {
    val description = varchar("description", 500)
    val quantity = integer("quantity")
    val unitPrice = long("unit_price")
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val invoice = reference(name = "invoice_id", foreign = InvoiceTable)
}

internal class InvoiceActivityEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<InvoiceActivityEntity>(InvoiceActivityTable)

    var description: String by InvoiceActivityTable.description
    var quantity: Int by InvoiceActivityTable.quantity
    var unitPrice: Long by InvoiceActivityTable.unitPrice
    var createdAt: Instant by InvoiceActivityTable.createdAt
    var updatedAt: Instant by InvoiceActivityTable.updatedAt
    var invoice by InvoiceEntity.Companion referencedOn InvoiceActivityTable.invoice
}