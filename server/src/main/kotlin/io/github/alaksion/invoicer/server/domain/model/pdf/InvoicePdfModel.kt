package io.github.alaksion.invoicer.server.domain.model.pdf

import kotlinx.datetime.LocalDate

data class InvoicePdfModel(
    val id: String,
    val path: String?,
    val status: InvoicePdfStatusModel,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
)

enum class InvoicePdfStatusModel {
    Ok,
    Pending,
    Failed;
}