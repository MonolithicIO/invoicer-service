package invoicer.alaksiondev.com.plugins

import DatabaseFactory
import invoicer.alaksiondev.com.datasource.InvoiceActivityDataSource
import invoicer.alaksiondev.com.datasource.InvoiceActivityDataSourceImpl
import invoicer.alaksiondev.com.datasource.InvoiceDataSource
import invoicer.alaksiondev.com.datasource.InvoiceDataSourceImpl
import invoicer.alaksiondev.com.datasource.InvoicePdfDataSource
import invoicer.alaksiondev.com.datasource.InvoicePdfDataSourceImpl
import invoicer.alaksiondev.com.files.filehandler.FileHandler
import invoicer.alaksiondev.com.files.filehandler.TempFileHandler
import invoicer.alaksiondev.com.files.pdfgenerator.OpenPdfGenerator
import invoicer.alaksiondev.com.files.pdfgenerator.PdfGenerator
import invoicer.alaksiondev.com.plugins.DITags.TEMP_FILE_HANDLER
import invoicer.alaksiondev.com.repository.InvoiceActivityRepository
import invoicer.alaksiondev.com.repository.InvoiceActivityRepositoryImpl
import invoicer.alaksiondev.com.repository.InvoicePdfRepository
import invoicer.alaksiondev.com.repository.InvoicePdfRepositoryImpl
import invoicer.alaksiondev.com.repository.InvoiceRepository
import invoicer.alaksiondev.com.repository.InvoiceRepositoryImpl
import invoicer.alaksiondev.com.services.CreateInvoicePdfService
import invoicer.alaksiondev.com.services.CreateInvoicePdfServiceImpl
import invoicer.alaksiondev.com.services.CreateInvoiceService
import invoicer.alaksiondev.com.services.CreateInvoiceServiceImpl
import io.ktor.server.application.Application
import kotlinx.coroutines.Dispatchers
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
        bindSingleton<InvoicePdfRepository> { InvoicePdfRepositoryImpl(dataSource = instance()) }

        bindSingleton<CreateInvoiceService> {
            CreateInvoiceServiceImpl(
                invoiceRepository = instance(),
                invoiceActivityRepository = instance()
            )
        }
        bindSingleton<CreateInvoicePdfService> {
            CreateInvoicePdfServiceImpl(
                invoiceRepository = instance(),
                invoicePdfRepository = instance(),
                pdfGenerator = instance(DITags.OPEN_PDF_GENERATOR),
                invoiceActivityRepository = instance()
            )
        }

        bindSingleton<PdfGenerator>(DITags.OPEN_PDF_GENERATOR) {
            OpenPdfGenerator(
                dispatcher = Dispatchers.IO,
                fileHandler = instance(TEMP_FILE_HANDLER)
            )
        }

        bindSingleton<FileHandler>(TEMP_FILE_HANDLER) { TempFileHandler }
    }
}

internal object DITags {
    const val OPEN_PDF_GENERATOR = "open-pdf"
    const val TEMP_FILE_HANDLER = "temp-file"
}