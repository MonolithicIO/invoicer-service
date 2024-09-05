package domains.invoice.data.api.di

import domains.invoice.data.api.datasource.InvoiceDataSource
import domains.invoice.data.api.datasource.InvoiceDataSourceImpl
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val invoiceDataModule = DI.Module("invoiceDataModule") {
    bindProvider<InvoiceDataSource> {
        InvoiceDataSourceImpl(
            dateProvider = instance()
        )
    }
}