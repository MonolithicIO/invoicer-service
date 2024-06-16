package invoicer.alaksiondev.com.app.plugins

import invoicer.alaksiondev.com.app.plugins.DITags.TEMP_FILE_HANDLER
import invoicer.alaksiondev.com.files.filehandler.FileHandler
import invoicer.alaksiondev.com.files.filehandler.TempFileHandler
import invoicer.alaksiondev.com.files.pdfgenerator.OpenPdfGenerator
import invoicer.alaksiondev.com.files.pdfgenerator.PdfGenerator
import invoicer.alaksiondev.com.repository.*
import invoicer.alaksiondev.com.service.*
import invoicer.alaksiondev.com.util.DateProvider
import invoicer.alaksiondev.com.util.DateProviderImplementation
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.kodein.di.ktor.di

fun Application.installDi() {
    di {

        bindSingleton<InvoiceRepository> { InvoiceRepositoryImpl(dateProvider = instance()) }
        bindSingleton<InvoiceActivityRepository> { InvoiceActivityRepositoryImpl(dateProvider = instance()) }
        bindSingleton<InvoicePdfRepository> { InvoicePdfRepositoryImpl() }

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
            )
        }

        bindSingleton<PdfGenerator>(DITags.OPEN_PDF_GENERATOR) {
            OpenPdfGenerator(
                dispatcher = Dispatchers.IO,
                fileHandler = instance(TEMP_FILE_HANDLER)
            )
        }

        bindSingleton<FileHandler>(TEMP_FILE_HANDLER) { TempFileHandler }

        bindSingleton<DateProvider> { DateProviderImplementation }

        bindSingleton<GetInvoiceByIdService> {
            GetInvoiceByIdServiceImpl(repository = instance())
        }
    }
}

internal object DITags {
    const val OPEN_PDF_GENERATOR = "open-pdf"
    const val TEMP_FILE_HANDLER = "temp-file"
}