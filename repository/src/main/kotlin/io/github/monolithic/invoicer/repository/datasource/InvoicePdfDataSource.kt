package io.github.monolithic.invoicer.repository.datasource

import io.github.monolithic.invoicer.repository.entities.InvoicePdfEntity
import io.github.monolithic.invoicer.repository.entities.InvoicePdfStatusEntity
import io.github.monolithic.invoicer.repository.entities.InvoicePdfTable
import io.github.monolithic.invoicer.repository.mapper.toModel
import kotlinx.datetime.Clock
import io.github.monolithic.invoicer.models.invoicepdf.InvoicePdfModel
import io.github.monolithic.invoicer.models.invoicepdf.InvoicePdfStatus
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.updateReturning
import java.util.*

interface InvoicePdfDataSource {
    suspend fun createInvoicePdf(
        invoiceId: UUID
    )

    suspend fun updateInvoicePdfState(
        invoiceId: UUID,
        status: InvoicePdfStatus,
        filePath: String,
    ): InvoicePdfModel

    suspend fun getInvoicePdf(
        invoiceId: UUID
    ): InvoicePdfModel?
}

internal class InvoicePdfDataSourceImpl(
    private val clock: Clock
) : InvoicePdfDataSource {

    override suspend fun createInvoicePdf(invoiceId: UUID) {
        return newSuspendedTransaction {
            InvoicePdfTable.insert {
                it[invoice] = invoiceId
                it[createdAt] = clock.now()
                it[updatedAt] = clock.now()
                it[filePath] = ""
                it[status] = InvoicePdfStatusEntity.pending
            }
        }
    }

    override suspend fun updateInvoicePdfState(
        invoiceId: UUID,
        status: InvoicePdfStatus,
        filePath: String
    ): InvoicePdfModel {
        return newSuspendedTransaction {
            InvoicePdfTable.updateReturning(
                where = {
                    InvoicePdfTable.invoice eq invoiceId
                }
            ) { table ->
                table[updatedAt] = clock.now()
                table[InvoicePdfTable.status] = InvoicePdfStatusEntity.Companion.fromModel(status)
                table[InvoicePdfTable.filePath] = filePath
            }.map {
                InvoicePdfEntity.Companion.wrapRow(it)
            }.first().toModel()
        }
    }

    override suspend fun getInvoicePdf(invoiceId: UUID): InvoicePdfModel? {
        return newSuspendedTransaction {
            InvoicePdfEntity.Companion.find {
                InvoicePdfTable.invoice eq invoiceId
            }.firstOrNull()?.let {
                InvoicePdfModel(
                    id = it.id.value,
                    invoiceId = it.invoice.id.value,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt,
                    path = it.filePath,
                    status = when (it.status) {
                        InvoicePdfStatusEntity.pending -> InvoicePdfStatus.Pending
                        InvoicePdfStatusEntity.success -> InvoicePdfStatus.Success
                        InvoicePdfStatusEntity.error -> InvoicePdfStatus.Failed
                    }
                )
            }
        }
    }
}
