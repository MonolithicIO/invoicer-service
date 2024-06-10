package invoicer.alaksiondev.com.domain.repository

import invoicer.alaksiondev.com.domain.models.createinvoice.CreateInvoiceModel
import invoicer.alaksiondev.com.domain.models.InvoiceModel

interface IInvoiceRepository {
    suspend fun createInvoice(data: CreateInvoiceModel): String

    suspend fun getInvoiceByExternalId(externalId: String): InvoiceModel?
}