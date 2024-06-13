package invoicer.alaksiondev.com.repository

import invoicer.alaksiondev.com.datasource.InvoiceActivityDataSource
import invoicer.alaksiondev.com.models.createinvoice.CreateInvoiceActivityModel

interface InvoiceActivityRepository {
    suspend fun createInvoiceActivities(
        list: List<CreateInvoiceActivityModel>,
        invoiceId: String,
    )
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

}