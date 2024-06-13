package invoicer.alaksiondev.com.datasource

import invoicer.alaksiondev.com.entities.InvoiceActivityEntity
import invoicer.alaksiondev.com.entities.InvoiceActivityTable
import invoicer.alaksiondev.com.models.createinvoice.CreateInvoiceActivityModel
import org.ktorm.database.Database
import org.ktorm.dsl.batchInsert
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import java.util.UUID

interface InvoiceActivityDataSource {
    suspend fun createInvoiceActivities(
        list: List<CreateInvoiceActivityModel>,
        invoiceId: String,
    )

    suspend fun getActivitiesByInvoiceId(
        invoiceId: String
    ): List<InvoiceActivityEntity>
}

internal class InvoiceActivityDataSourceImpl(
    private val database: Database
) : InvoiceActivityDataSource {

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

    override suspend fun getActivitiesByInvoiceId(invoiceId: String): List<InvoiceActivityEntity> {
        val query = database
            .from(InvoiceActivityTable)
            .select()
            .where {
                InvoiceActivityTable.invoiceId eq UUID.fromString(invoiceId)
            }
        return query.map { queryItem ->
            InvoiceActivityEntity {
                id = queryItem[InvoiceActivityTable.id] ?: throw IllegalStateException("")
                description =
                    queryItem[InvoiceActivityTable.description] ?: throw IllegalStateException("")
                quantity =
                    queryItem[InvoiceActivityTable.quantity] ?: throw IllegalStateException("")
                unitPrice =
                    queryItem[InvoiceActivityTable.unitPrice] ?: throw IllegalStateException("")
                createdAt =
                    queryItem[InvoiceActivityTable.createdAt] ?: throw IllegalStateException("")
                updatedAt =
                    queryItem[InvoiceActivityTable.updatedAt] ?: throw IllegalStateException("")
                this.invoiceId =
                    queryItem[InvoiceActivityTable.invoiceId] ?: throw IllegalStateException("")
            }
        }
    }
}