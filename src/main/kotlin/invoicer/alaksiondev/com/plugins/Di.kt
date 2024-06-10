package invoicer.alaksiondev.com.plugins

import DatabaseFactory
import invoicer.alaksiondev.com.data.datasource.IInvoiceDataSource
import invoicer.alaksiondev.com.data.datasource.InvoiceDataSource
import invoicer.alaksiondev.com.data.repository.InvoiceRepository
import invoicer.alaksiondev.com.data.services.CreateInvoiceService
import invoicer.alaksiondev.com.data.services.ICreateInvoiceService
import invoicer.alaksiondev.com.domain.repository.IInvoiceRepository
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
        bindSingleton<ICreateInvoiceService> { CreateInvoiceService(repository = instance()) }
    }
}