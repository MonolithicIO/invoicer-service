package invoicer.alaksiondev.com.data.repository

import invoicer.alaksiondev.com.data.datasource.IInvoiceDataSource
import invoicer.alaksiondev.com.data.entities.toInvoiceModel
import invoicer.alaksiondev.com.domain.models.createinvoice.CreateInvoiceModel
import invoicer.alaksiondev.com.domain.models.InvoiceModel
import invoicer.alaksiondev.com.domain.repository.IInvoiceRepository


internal class InvoiceRepository(
    private val dataSource: IInvoiceDataSource
) : IInvoiceRepository {

    override suspend fun createInvoice(data: CreateInvoiceModel): String {
        return dataSource.createInvoice(model = data)
    }

    override suspend fun getInvoiceByExternalId(externalId: String): InvoiceModel? {
        return dataSource.getInvoiceByExternalId(externalId = externalId)?.toInvoiceModel()
    }

}