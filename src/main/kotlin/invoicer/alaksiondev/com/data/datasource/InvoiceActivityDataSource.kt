package invoicer.alaksiondev.com.data.datasource

import invoicer.alaksiondev.com.data.entities.InvoiceActivityTable
import invoicer.alaksiondev.com.domain.models.createinvoice.CreateInvoiceActivityModel
import org.ktorm.database.Database
import org.ktorm.dsl.batchInsert
import java.util.UUID

interface IInvoiceActivityDataSource {
    suspend fun createInvoiceActivities(
        list: List<CreateInvoiceActivityModel>,
        invoiceId: String,
    )
}

internal class InvoiceActivityDataSource(
    private val database: Database
) : IInvoiceActivityDataSource {

    override suspend fun createInvoiceActivities(
        list: List<CreateInvoiceActivityModel>,
        invoiceId: String
    ) {
        database.batchInsert(InvoiceActivityTable) {
            list.forEach { invoiceModel ->
                item { invoiceEntity ->
                    set(invoiceEntity.invoiceId, UUID.fromString(invoiceId))
                    set(invoiceEntity.quantity, invoiceModel.quantity)
                    set(invoiceEntity.unitPrice, invoiceModel.unitPrice)
                    set(invoiceEntity.description, invoiceModel.description)
                }
            }
        }
    }
}