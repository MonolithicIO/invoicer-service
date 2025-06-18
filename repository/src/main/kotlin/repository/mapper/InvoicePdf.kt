package repository.mapper

import models.invoicepdf.InvoicePdfModel
import repository.entities.legacy.InvoicePdfEntity
import repository.entities.legacy.InvoicePdfStatusEntity

internal fun InvoicePdfEntity.toModel(): InvoicePdfModel = InvoicePdfModel(
    id = id.value,
    invoiceId = invoice.id.value,
    createdAt = createdAt,
    updatedAt = updatedAt,
    path = filePath,
    status = when (status) {
        InvoicePdfStatusEntity.pending -> models.invoicepdf.InvoicePdfStatus.Pending
        InvoicePdfStatusEntity.success -> models.invoicepdf.InvoicePdfStatus.Success
        InvoicePdfStatusEntity.error -> models.invoicepdf.InvoicePdfStatus.Failed
    }
)