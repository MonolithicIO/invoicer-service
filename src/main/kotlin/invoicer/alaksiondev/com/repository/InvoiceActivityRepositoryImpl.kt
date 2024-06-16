package invoicer.alaksiondev.com.repository

import invoicer.alaksiondev.com.entities.InvoiceActivityEntity
import invoicer.alaksiondev.com.entities.InvoiceActivityTable
import invoicer.alaksiondev.com.models.createinvoice.CreateInvoiceActivityModel
import invoicer.alaksiondev.com.util.DateProvider
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

internal interface InvoiceActivityRepository {
    suspend fun createInvoiceActivities(
        list: List<CreateInvoiceActivityModel>,
        invoiceId: String,
    )

    suspend fun getActivitiesByInvoiceId(
        invoiceId: String
    ): List<InvoiceActivityEntity>
}

internal class InvoiceActivityRepositoryImpl(
    private val dateProvider: DateProvider
) : InvoiceActivityRepository {

    override suspend fun createInvoiceActivities(
        list: List<CreateInvoiceActivityModel>,
        invoiceId: String
    ) {
        transaction {
            InvoiceActivityTable.batchInsert(list) { item ->
                this[InvoiceActivityTable.invoice] = UUID.fromString(invoiceId)
                this[InvoiceActivityTable.quantity] = item.quantity
                this[InvoiceActivityTable.unitPrice] = item.unitPrice
                this[InvoiceActivityTable.description] = item.description
                this[InvoiceActivityTable.createdAt] = dateProvider.now()
                this[InvoiceActivityTable.updatedAt] = dateProvider.now()
            }
        }
    }

    override suspend fun getActivitiesByInvoiceId(invoiceId: String): List<InvoiceActivityEntity> {
        return transaction {
            InvoiceActivityEntity.find {
                InvoiceActivityTable.invoice eq UUID.fromString(invoiceId)
            }
        }.toList()
    }
}