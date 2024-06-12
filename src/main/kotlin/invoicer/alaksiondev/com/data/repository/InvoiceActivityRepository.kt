package invoicer.alaksiondev.com.data.repository

import invoicer.alaksiondev.com.data.datasource.IInvoiceActivityDataSource
import invoicer.alaksiondev.com.domain.models.createinvoice.CreateInvoiceActivityModel
import invoicer.alaksiondev.com.domain.repository.IInvoiceActivityRepository

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