package invoicer.alaksiondev.com.data.services

import invoicer.alaksiondev.com.domain.models.createinvoice.CreateInvoiceModel
import invoicer.alaksiondev.com.domain.models.createinvoice.CreateInvoiceResponseModel
import invoicer.alaksiondev.com.domain.repository.IInvoiceRepository

interface ICreateInvoiceService {
    suspend fun createInvoice(model: CreateInvoiceModel): CreateInvoiceResponseModel
}

internal class CreateInvoiceService(
    private val repository: IInvoiceRepository
) : ICreateInvoiceService {

    override suspend fun createInvoice(model: CreateInvoiceModel): CreateInvoiceResponseModel {
        if (repository.getInvoiceByExternalId(externalId = model.externalId) != null) {
            throw Error()
        }

        val response = repository.createInvoice(data = model)
        return CreateInvoiceResponseModel(
            externalInvoiceId = model.externalId,
            invoiceId = response
        )
    }

}