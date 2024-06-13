package invoicer.alaksiondev.com.plugins

import DatabaseFactory
import invoicer.alaksiondev.com.datasource.InvoiceActivityDataSource
import invoicer.alaksiondev.com.datasource.InvoiceDataSource
import invoicer.alaksiondev.com.datasource.InvoiceActivityDataSourceImpl
import invoicer.alaksiondev.com.datasource.InvoiceDataSourceImpl
import invoicer.alaksiondev.com.datasource.InvoicePdfDataSource
import invoicer.alaksiondev.com.datasource.InvoicePdfDataSourceImpl
import invoicer.alaksiondev.com.repository.InvoiceActivityRepository
import invoicer.alaksiondev.com.repository.InvoiceRepository
import invoicer.alaksiondev.com.repository.InvoiceActivityRepositoryImpl
import invoicer.alaksiondev.com.repository.InvoiceRepositoryImpl
import invoicer.alaksiondev.com.services.CreateInvoiceServiceImpl
import invoicer.alaksiondev.com.services.CreateInvoiceService
import io.ktor.server.application.Application
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.ktorm.database.Database

fun Application.installDi() {
    di {
        bindSingleton<Database> { DatabaseFactory.database }

        bindSingleton<InvoiceDataSource> { InvoiceDataSourceImpl(database = instance()) }
        bindSingleton<InvoiceRepository> { InvoiceRepositoryImpl(dataSource = instance()) }

        bindSingleton<InvoiceActivityDataSource> { InvoiceActivityDataSourceImpl(database = instance()) }
        bindSingleton<InvoiceActivityRepository> { InvoiceActivityRepositoryImpl(dataSource = instance()) }

        bindSingleton<InvoicePdfDataSource> { InvoicePdfDataSourceImpl(database = instance()) }

        bindSingleton<CreateInvoiceService> {
            CreateInvoiceServiceImpl(
                invoiceRepository = instance(),
                invoiceActivityRepository = instance()
            )
        }
    }
}