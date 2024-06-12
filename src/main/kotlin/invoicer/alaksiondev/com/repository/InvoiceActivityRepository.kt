package invoicer.alaksiondev.com.repository

import invoicer.alaksiondev.com.datasource.IInvoiceActivityDataSource
import invoicer.alaksiondev.com.models.createinvoice.CreateInvoiceActivityModel

interface IInvoiceActivityRepository {
    suspend fun createInvoiceActivities(
        list: List<CreateInvoiceActivityModel>,
        invoiceId: String,
    )
}

internal class InvoiceActivityRepository(
    private val dataSource: IInvoiceActivityDataSource
) : IInvoiceActivityRepository {

    override suspend fun createInvoiceActivities(
        list: List<CreateInvoiceActivityModel>,
        invoiceId: String
    ) {
        dataSource.createInvoiceActivities(list = list, invoiceId = invoiceId)
    }

}