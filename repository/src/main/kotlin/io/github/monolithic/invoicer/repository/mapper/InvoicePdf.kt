package io.github.monolithic.invoicer.repository.mapper

import io.github.monolithic.invoicer.models.invoicepdf.InvoicePdfModel
import io.github.monolithic.invoicer.models.invoicepdf.InvoicePdfStatus
import io.github.monolithic.invoicer.repository.entities.InvoicePdfEntity
import io.github.monolithic.invoicer.repository.entities.InvoicePdfStatusEntity

internal fun InvoicePdfEntity.toModel(): InvoicePdfModel = InvoicePdfModel(
    id = id.value,
    invoiceId = invoice.id.value,
    createdAt = createdAt,
    updatedAt = updatedAt,
    path = filePath,
    status = when (status) {
        InvoicePdfStatusEntity.pending -> InvoicePdfStatus.Pending
        InvoicePdfStatusEntity.success -> InvoicePdfStatus.Success
        InvoicePdfStatusEntity.error -> InvoicePdfStatus.Failed
    }
)
