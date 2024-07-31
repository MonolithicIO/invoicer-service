package io.github.alaksion.invoicer.server.data.entities

import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.date
import java.util.*

internal object InvoiceActivityTable : UUIDTable("t_invoice_activity") {
    val description = varchar("description", 500)
    val quantity = integer("quantity")
    val unitPrice = long("unit_price")
    val createdAt = date("created_at")
    val updatedAt = date("updated_at")
    val invoice = reference(name = "invoice_id", foreign = InvoiceTable, onDelete = ReferenceOption.CASCADE)
}

internal class InvoiceActivityEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<InvoiceActivityEntity>(InvoiceActivityTable)

    var description: String by InvoiceActivityTable.description
    var quantity: Int by InvoiceActivityTable.quantity
    var unitPrice: Long by InvoiceActivityTable.unitPrice
    var createdAt: LocalDate by InvoiceActivityTable.createdAt
    var updatedAt: LocalDate by InvoiceActivityTable.updatedAt
    var invoice by InvoiceEntity referencedOn InvoiceActivityTable.invoice
}