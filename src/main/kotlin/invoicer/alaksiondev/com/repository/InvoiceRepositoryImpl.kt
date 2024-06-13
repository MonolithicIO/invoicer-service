package invoicer.alaksiondev.com.repository

import invoicer.alaksiondev.com.datasource.InvoiceDataSource
import invoicer.alaksiondev.com.entities.toInvoiceModel
import invoicer.alaksiondev.com.models.InvoiceModel
import invoicer.alaksiondev.com.models.createinvoice.CreateInvoiceModel

interface InvoiceRepository {
    suspend fun createInvoice(data: CreateInvoiceModel): String

    suspend fun getInvoiceByExternalId(externalId: String): InvoiceModel?
}


internal class InvoiceRepositoryImpl(
    private val dataSource: InvoiceDataSource
) : InvoiceRepository {

    override suspend fun createInvoice(data: CreateInvoiceModel): String {
        return dataSource.createInvoice(model = data)
    }

    override suspend fun getInvoiceByExternalId(externalId: String): InvoiceModel? {
        return dataSource.getInvoiceByExternalId(externalId = externalId)?.toInvoiceModel()
    }

}