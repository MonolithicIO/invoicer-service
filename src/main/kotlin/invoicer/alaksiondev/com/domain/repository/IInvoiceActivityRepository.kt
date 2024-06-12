package invoicer.alaksiondev.com.domain.repository

import invoicer.alaksiondev.com.domain.models.createinvoice.CreateInvoiceActivityModel

interface IInvoiceActivityRepository {
    suspend fun createInvoiceActivities(
        list: List<CreateInvoiceActivityModel>,
        invoiceId: String,
    )
}