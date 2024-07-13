package io.github.alaksion.invoicer.server.app.plugins

import io.github.alaksion.invoicer.server.app.plugins.DITags.TEMP_FILE_HANDLER
import io.github.alaksion.invoicer.server.data.datasource.InvoiceDataSource
import io.github.alaksion.invoicer.server.data.datasource.InvoiceDataSourceImpl
import io.github.alaksion.invoicer.server.data.repository.InvoiceRepositoryImpl
import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.domain.usecase.*
import io.github.alaksion.invoicer.server.files.filehandler.FileHandler
import io.github.alaksion.invoicer.server.files.filehandler.TempFileHandler
import io.github.alaksion.invoicer.server.files.pdfgenerator.OpenPdfGenerator
import io.github.alaksion.invoicer.server.files.pdfgenerator.PdfGenerator
import io.github.alaksion.invoicer.server.repository.InvoicePdfRepository
import io.github.alaksion.invoicer.server.repository.InvoicePdfRepositoryImpl
import io.github.alaksion.invoicer.server.util.DateProvider
import io.github.alaksion.invoicer.server.util.DateProviderImplementation
import io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.request.CreateInvoiceRequestViewModelReceiver
import io.github.alaksion.invoicer.server.view.viewmodel.createinvoice.request.CreateInvoiceRequestViewModelReceiverImpl
import io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.request.GetInvoicesFilterViewModelReceiver
import io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.request.GetInvoicesFilterViewModelReceiverImpl
import io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.response.GetInvoicesViewModelSender
import io.github.alaksion.invoicer.server.view.viewmodel.getinvoices.response.GetInvoicesViewModelSenderImpl
import io.github.alaksion.invoicer.server.view.viewmodel.invoicedetails.response.InvoiceDetailsViewModelSender
import io.github.alaksion.invoicer.server.view.viewmodel.invoicedetails.response.InvoiceDetailsViewModelSenderImpl
import io.ktor.server.application.*
import org.kodein.di.bindProvider
import org.kodein.di.instance
import org.kodein.di.ktor.di

fun Application.installDi() {
    di {

        bindProvider<InvoiceRepository> { InvoiceRepositoryImpl(dataSource = instance()) }
        bindProvider<InvoiceDataSource> { InvoiceDataSourceImpl(dateProvider = instance()) }
        bindProvider<CreateInvoiceRequestViewModelReceiver> { CreateInvoiceRequestViewModelReceiverImpl() }

        bindProvider<GetInvoicesFilterViewModelReceiver> { GetInvoicesFilterViewModelReceiverImpl() }
        bindProvider<GetInvoicesViewModelSender> { GetInvoicesViewModelSenderImpl() }
        bindProvider<InvoiceDetailsViewModelSender> { InvoiceDetailsViewModelSenderImpl() }

        bindProvider<InvoicePdfRepository> { InvoicePdfRepositoryImpl() }

        bindProvider<CreateInvoiceUseCase> {
            CreateInvoiceUseCaseImpl(
                invoiceRepository = instance(),
            )
        }
        bindProvider<CreateInvoicePdfUseCase> {
            CreateInvoicePdfUseCaseImpl(
                invoiceRepository = instance(),
                invoicePdfRepository = instance(),
                pdfGenerator = instance(DITags.OPEN_PDF_GENERATOR),
            )
        }
        bindProvider<GetInvoicesUseCase> {
            GetInvoicesUseCaseImpl(
                repository = instance()
            )
        }

        bindProvider<PdfGenerator>(DITags.OPEN_PDF_GENERATOR) {
            OpenPdfGenerator(
                fileHandler = instance(TEMP_FILE_HANDLER)
            )
        }

        bindProvider<FileHandler>(TEMP_FILE_HANDLER) { TempFileHandler }

        bindProvider<DateProvider> { DateProviderImplementation }

        bindProvider<GetInvoiceByIdUseCase> {
            GetInvoiceByIdUseCaseImpl(repository = instance())
        }
    }
}

internal object DITags {
    const val OPEN_PDF_GENERATOR = "open-pdf"
    const val TEMP_FILE_HANDLER = "temp-file"
}