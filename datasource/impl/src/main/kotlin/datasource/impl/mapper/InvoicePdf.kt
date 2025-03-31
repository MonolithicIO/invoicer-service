package datasource.impl.mapper

import datasource.impl.entities.InvoicePdfEntity
import models.invoicepdf.InvoicePdfModel

internal fun InvoicePdfEntity.toModel(): InvoicePdfModel = InvoicePdfModel(
    id = id.value.toString(),
    invoiceId = invoice.id.value.toString(),
    createdAt = createdAt,
    updatedAt = updatedAt,
    path = filePath
)