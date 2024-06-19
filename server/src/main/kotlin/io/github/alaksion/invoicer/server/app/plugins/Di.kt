package io.github.alaksion.invoicer.server.app.plugins

import io.github.alaksion.invoicer.server.app.plugins.DITags.TEMP_FILE_HANDLER
import io.github.alaksion.invoicer.server.files.filehandler.FileHandler
import io.github.alaksion.invoicer.server.files.filehandler.TempFileHandler
import io.github.alaksion.invoicer.server.files.pdfgenerator.OpenPdfGenerator
import io.github.alaksion.invoicer.server.files.pdfgenerator.PdfGenerator
import io.github.alaksion.invoicer.server.util.DateProvider
import io.github.alaksion.invoicer.server.util.DateProviderImplementation
import io.github.alaksion.invoicer.server.repository.InvoiceActivityRepository
import io.github.alaksion.invoicer.server.repository.InvoiceActivityRepositoryImpl
import io.github.alaksion.invoicer.server.repository.InvoicePdfRepository
import io.github.alaksion.invoicer.server.repository.InvoicePdfRepositoryImpl
import io.github.alaksion.invoicer.server.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.repository.InvoiceRepositoryImpl
import io.github.alaksion.invoicer.server.service.*
import io.github.alaksion.invoicer.server.service.CreateInvoicePdfServiceImpl
import io.github.alaksion.invoicer.server.service.CreateInvoiceService
import io.github.alaksion.invoicer.server.service.CreateInvoiceServiceImpl
import io.github.alaksion.invoicer.server.service.GetInvoiceByIdService
import io.github.alaksion.invoicer.server.service.GetInvoiceByIdServiceImpl
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