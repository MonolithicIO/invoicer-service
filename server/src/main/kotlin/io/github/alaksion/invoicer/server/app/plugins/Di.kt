package io.github.alaksion.invoicer.server.app.plugins

import foundation.api.di.secretsModule
import foundation.validator.api.di.validatorModule
import io.github.alaksion.invoicer.server.app.plugins.DITags.TEMP_FILE_HANDLER
import io.github.alaksion.invoicer.server.files.filehandler.FileHandler
import io.github.alaksion.invoicer.server.files.filehandler.TempFileHandler
import io.github.alaksion.invoicer.server.files.pdfgenerator.OpenPdfGenerator
import io.github.alaksion.invoicer.server.files.pdfgenerator.PdfGenerator
import io.github.alaksion.invoicer.server.view.viewmodel.invoicedetails.response.InvoiceDetailsViewModelSender
import io.github.alaksion.invoicer.server.view.viewmodel.invoicedetails.response.InvoiceDetailsViewModelSenderImpl
import io.ktor.server.application.Application
import org.kodein.di.bindProvider
import org.kodein.di.instance
import org.kodein.di.ktor.di
import repository.api.di.repositoryModule
import services.impl.di.servicesImplModule
import utils.authentication.api.di.utilsAuthenticationModule
import utils.date.api.di.utilsDateModule
import utils.password.di.utilPasswordDi

fun Application.installDi() {
    di {
        import(utilPasswordDi)
        import(utilsDateModule)
        import(utilsAuthenticationModule)
        import(validatorModule)
        import(secretsModule)
        import(servicesImplModule)
        import(repositoryModule)

        bindProvider<InvoiceDetailsViewModelSender> { InvoiceDetailsViewModelSenderImpl() }

        bindProvider<PdfGenerator>(DITags.OPEN_PDF_GENERATOR) {
            OpenPdfGenerator(
                fileHandler = instance(TEMP_FILE_HANDLER)
            )
        }

        bindProvider<FileHandler>(TEMP_FILE_HANDLER) { TempFileHandler }
    }
}

internal object DITags {
    const val OPEN_PDF_GENERATOR = "open-pdf"
    const val TEMP_FILE_HANDLER = "temp-file"
}