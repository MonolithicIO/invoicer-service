package invoicer.alaksiondev.com.data.entities

import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import java.util.UUID

object InvoiceServiceTable : Table<InvoiceServiceEntity>("t_invoice_service") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val description = varchar("description").bindTo { it.description }
    val quantity = int("quantity").bindTo { it.quantity }
    val unitPrice = long("unit_price").bindTo { it.unitPrice }
    val invoiceId = uuid("invoice_id").references(InvoiceTable) {
        it.invoice
    }
}

interface InvoiceServiceEntity : Entity<InvoiceServiceEntity> {
    companion object : Entity.Factory<InvoiceServiceEntity>()

    val id: UUID
    var description: String
    var quantity: Int
    var unitPrice: Long
    val invoice: InvoiceEntity
}