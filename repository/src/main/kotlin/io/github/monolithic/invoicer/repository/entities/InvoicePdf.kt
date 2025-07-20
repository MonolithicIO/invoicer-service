package io.github.monolithic.invoicer.repository.entities

import io.github.monolithic.invoicer.models.invoicepdf.InvoicePdfStatus
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.postgresql.util.PGobject
import java.util.*

private class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}

internal object InvoicePdfTable : UUIDTable("t_invoice_pdf") {
    val filePath = varchar("file_path", Int.MAX_VALUE)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
    val invoice = reference(name = "invoice_id", foreign = InvoiceTable, onDelete = ReferenceOption.CASCADE)
    val status = customEnumeration(
        "status",
        "invoice_pdf_status",
        { value ->
            when (value) {
                is String -> InvoicePdfStatusEntity.valueOf(value)
                else -> error("Can't convert ENUM_COLUMN")
            }
        },
        { PGEnum("invoice_pdf_status", it) }
    )
}

internal class InvoicePdfEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<InvoicePdfEntity>(InvoicePdfTable)

    var filePath by InvoicePdfTable.filePath
    var createdAt by InvoicePdfTable.createdAt
    var updatedAt by InvoicePdfTable.updatedAt
    var status by InvoicePdfTable.status
    val invoice by InvoiceEntity.Companion referencedOn InvoicePdfTable.invoice
}

internal enum class InvoicePdfStatusEntity {
    pending,
    error,
    success;

    companion object {
        fun fromModel(model: InvoicePdfStatus): InvoicePdfStatusEntity {
            return when (model) {
                InvoicePdfStatus.Pending -> pending
                InvoicePdfStatus.Failed -> error
                InvoicePdfStatus.Success -> success
            }
        }
    }
}
