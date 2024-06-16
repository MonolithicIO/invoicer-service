package invoicer.alaksiondev.com.mappers

import invoicer.alaksiondev.com.entities.InvoiceActivityEntity
import invoicer.alaksiondev.com.models.InvoiceActivityModel

internal fun InvoiceActivityEntity.toDomain(
    mapInvoice: Boolean
): InvoiceActivityModel {
    return InvoiceActivityModel(
        description = this.description,
        quantity = this.quantity,
        unitPrice = this.unitPrice,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
        invoice = if (mapInvoice) this.invoice.toDomain(mapActivities = false) else null
    )
}
