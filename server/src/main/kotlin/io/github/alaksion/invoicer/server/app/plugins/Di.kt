package io.github.alaksion.invoicer.server.app.plugins

import io.github.alaksion.invoicer.server.app.plugins.DITags.TEMP_FILE_HANDLER
import io.github.alaksion.invoicer.server.data.datasource.InvoiceDataSource
import io.github.alaksion.invoicer.server.data.datasource.InvoiceDataSourceImpl
import io.github.alaksion.invoicer.server.data.datasource.UserDataSource
import io.github.alaksion.invoicer.server.data.datasource.UserDataSourceImpl
import io.github.alaksion.invoicer.server.data.repository.InvoicePdfRepositoryImpl
import io.github.alaksion.invoicer.server.data.repository.InvoiceRepositoryImpl
import io.github.alaksion.invoicer.server.domain.repository.InvoicePdfRepository
import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.domain.repository.UserRepository
import io.github.alaksion.invoicer.server.domain.repository.UserRepositoryImpl
import io.github.alaksion.invoicer.server.domain.usecase.CreateInvoicePdfUseCase
import io.github.alaksion.invoicer.server.domain.usecase.CreateInvoicePdfUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.invoice.*
import io.github.alaksion.invoicer.server.domain.usecase.login.LoginUseCase
import io.github.alaksion.invoicer.server.domain.usecase.login.LoginUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.user.CreateUserUseCase
import io.github.alaksion.invoicer.server.domain.usecase.user.CreateUserUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.user.DeleteUserUseCase
import io.github.alaksion.invoicer.server.domain.usecase.user.DeleteUserUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByEmailUseCase
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByEmailUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByIdUseCase
import io.github.alaksion.invoicer.server.domain.usecase.user.GetUserByIdUseCaseImpl
import io.github.alaksion.invoicer.server.files.filehandler.FileHandler
import io.github.alaksion.invoicer.server.files.filehandler.TempFileHandler
import io.github.alaksion.invoicer.server.files.pdfgenerator.OpenPdfGenerator
import io.github.alaksion.invoicer.server.files.pdfgenerator.PdfGenerator
import io.github.alaksion.invoicer.server.view.viewmodel.invoicedetails.response.InvoiceDetailsViewModelSender
import io.github.alaksion.invoicer.server.view.viewmodel.invoicedetails.response.InvoiceDetailsViewModelSenderImpl
import io.ktor.server.application.*
import org.kodein.di.bindProvider
import org.kodein.di.instance
import org.kodein.di.ktor.di
import utils.authentication.api.di.utilsAuthenticationModule
import utils.date.api.di.utilsDateModule
import utils.password.di.utilPasswordDi

fun Application.installDi() {
    di {
        import(utilPasswordDi)
        import(utilsDateModule)
        import(utilsAuthenticationModule)

        bindProvider<InvoiceRepository> { InvoiceRepositoryImpl(dataSource = instance()) }
        bindProvider<InvoiceDataSource> { InvoiceDataSourceImpl(dateProvider = instance()) }
        bindProvider<InvoiceDetailsViewModelSender> { InvoiceDetailsViewModelSenderImpl() }
        bindProvider<UserDataSource> { UserDataSourceImpl() }
        bindProvider<UserRepository> {
            UserRepositoryImpl(
                dataSource = instance()
            )
        }

        bindProvider<InvoicePdfRepository> {
            InvoicePdfRepositoryImpl(
                dataSource = instance()
            )
        }

        bindProvider<CreateInvoiceUseCase> {
            CreateInvoiceUseCaseImpl(
                invoiceRepository = instance(),
                dateProvider = instance(),
                getUserByIdUseCase = instance()
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
        bindProvider<DeleteInvoiceUseCase> {
            DeleteInvoiceUseCaseImpl(
                repository = instance(),
                getInvoiceByIdUseCase = instance(),
                getUserByIdUseCase = instance()
            )
        }

        bindProvider<PdfGenerator>(DITags.OPEN_PDF_GENERATOR) {
            OpenPdfGenerator(
                fileHandler = instance(TEMP_FILE_HANDLER)
            )
        }

        bindProvider<FileHandler>(TEMP_FILE_HANDLER) { TempFileHandler }

        bindProvider<GetInvoiceByIdUseCase> {
            GetInvoiceByIdUseCaseImpl(
                repository = instance(),
                getUserByIdUseCase = instance()
            )
        }

        bindProvider<GetUserByEmailUseCase> { GetUserByEmailUseCaseImpl(userRepository = instance()) }

        bindProvider<CreateUserUseCase> {
            CreateUserUseCaseImpl(
                getUserByEmailUseCase = instance(),
                passwordValidator = instance(),
                passwordEncryption = instance(),
                repository = instance()
            )
        }

        bindProvider<LoginUseCase> {
            LoginUseCaseImpl(
                getUserByEmailUseCase = instance(),
                authTokenManager = instance(),
                passwordEncryption = instance()
            )
        }

        bindProvider<DeleteUserUseCase> {
            DeleteUserUseCaseImpl(
                userRepository = instance(),
                getUserByIdUseCase = instance()
            )
        }

        bindProvider<GetUserByIdUseCase> {
            GetUserByIdUseCaseImpl(
                userRepository = instance()
            )

        }
    }
}

internal object DITags {
    const val OPEN_PDF_GENERATOR = "open-pdf"
    const val TEMP_FILE_HANDLER = "temp-file"
}