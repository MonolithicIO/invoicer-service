package invoicer.alaksiondev.com.datasource

interface IInvoicePdfDataSource {
    suspend fun generateInvoicePdf(invoiceId: String)
}

class InvoicePdfDataSource : IInvoicePdfDataSource {
    override suspend fun generateInvoicePdf(invoiceId: String) {
        TODO("Not yet implemented")
    }
}