package invoicer.alaksiondev.com.plugins

import DatabaseFactory
import invoicer.alaksiondev.com.datasource.IInvoiceActivityDataSource
import invoicer.alaksiondev.com.datasource.IInvoiceDataSource
import invoicer.alaksiondev.com.datasource.InvoiceActivityDataSource
import invoicer.alaksiondev.com.datasource.InvoiceDataSource
import invoicer.alaksiondev.com.repository.IInvoiceActivityRepository
import invoicer.alaksiondev.com.repository.IInvoiceRepository
import invoicer.alaksiondev.com.repository.InvoiceActivityRepository
import invoicer.alaksiondev.com.repository.InvoiceRepository
import invoicer.alaksiondev.com.services.CreateInvoiceService
import invoicer.alaksiondev.com.services.ICreateInvoiceService
import io.ktor.server.application.Application
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.kodein.di.ktor.di
import org.ktorm.database.Database

fun Application.installDi() {
    di {
        bindSingleton<Database> { DatabaseFactory.database }

        bindSingleton<IInvoiceDataSource> { InvoiceDataSource(database = instance()) }
        bindSingleton<IInvoiceRepository> { InvoiceRepository(dataSource = instance()) }

        bindSingleton<IInvoiceActivityDataSource> { InvoiceActivityDataSource(database = instance()) }
        bindSingleton<IInvoiceActivityRepository> { InvoiceActivityRepository(dataSource = instance()) }

        bindSingleton<ICreateInvoiceService> {
            CreateInvoiceService(
                invoiceRepository = instance(),
                invoiceActivityRepository = instance()
            )
        }
    }
}