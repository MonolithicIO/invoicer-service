package repository.mapper

import models.invoicepdf.InvoicePdfModel
import repository.entities.InvoicePdfEntity
import repository.entities.InvoicePdfStatusEntity

internal fun InvoicePdfEntity.toModel(): InvoicePdfModel = InvoicePdfModel(
    id = id.value,
    invoiceId = invoice.id.value,
    createdAt = createdAt,
    updatedAt = updatedAt,
    path = filePath,
    status = when (status) {
        InvoicePdfStatusEntity.Pending -> models.invoicepdf.InvoicePdfStatus.Pending
        InvoicePdfStatusEntity.Success -> models.invoicepdf.InvoicePdfStatus.Success
        InvoicePdfStatusEntity.Error -> models.invoicepdf.InvoicePdfStatus.Failed
    }
)
