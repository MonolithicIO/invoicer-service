package invoicer.alaksiondev.com.entities

import invoicer.alaksiondev.com.models.InvoiceActivityModel
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.long
import org.ktorm.schema.uuid
import org.ktorm.schema.varchar
import java.time.LocalDate
import java.util.UUID

object InvoiceActivityTable : Table<InvoiceActivityEntity>("t_invoice_activity") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val description = varchar("description").bindTo { it.description }
    val quantity = int("quantity").bindTo { it.quantity }
    val unitPrice = long("unit_price").bindTo { it.unitPrice }
    val invoiceId = uuid("invoice_id").bindTo { it.invoiceId }
    val createdAt = date("created_at").bindTo { it.createdAt }
    val updatedAt = date("updated_at").bindTo { it.updatedAt }
}

interface InvoiceActivityEntity : Entity<InvoiceActivityEntity> {
    companion object : Entity.Factory<InvoiceActivityEntity>()

    var id: UUID
    var description: String
    var quantity: Int
    var unitPrice: Long
    var createdAt: LocalDate
    var updatedAt: LocalDate
    var invoiceId: UUID
}

fun InvoiceActivityEntity.toDomain(): InvoiceActivityModel = InvoiceActivityModel(
    description = this.description,
    quantity = this.quantity,
    unitPrice = this.unitPrice,
    createdAt = this.createdAt.toString(),
    updatedAt = this.updatedAt.toString(),
)