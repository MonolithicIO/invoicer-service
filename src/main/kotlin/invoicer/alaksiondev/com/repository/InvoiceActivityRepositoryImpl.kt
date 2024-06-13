package invoicer.alaksiondev.com.repository

import invoicer.alaksiondev.com.datasource.InvoiceActivityDataSource
import invoicer.alaksiondev.com.entities.toDomain
import invoicer.alaksiondev.com.models.InvoiceActivityModel
import invoicer.alaksiondev.com.models.createinvoice.CreateInvoiceActivityModel

interface InvoiceActivityRepository {
    suspend fun createInvoiceActivities(
        list: List<CreateInvoiceActivityModel>,
        invoiceId: String,
    )

    suspend fun getActivitiesByInvoiceId(
        invoiceId: String
    ): List<InvoiceActivityModel>
}

internal class InvoiceActivityRepositoryImpl(
    private val dataSource: InvoiceActivityDataSource
) : InvoiceActivityRepository {

    override suspend fun createInvoiceActivities(
        list: List<CreateInvoiceActivityModel>,
        invoiceId: String
    ) {
        dataSource.createInvoiceActivities(list = list, invoiceId = invoiceId)
    }

    override suspend fun getActivitiesByInvoiceId(invoiceId: String): List<InvoiceActivityModel> {
        return dataSource.getActivitiesByInvoiceId(invoiceId = invoiceId).map {
            it.toDomain()
        }
    }

}