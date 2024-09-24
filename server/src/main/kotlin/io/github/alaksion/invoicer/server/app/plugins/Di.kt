package io.github.alaksion.invoicer.server.app.plugins

import foundation.api.di.secretsModule
import foundation.validator.api.di.validatorModule
import io.github.alaksion.invoicer.server.app.plugins.DITags.TEMP_FILE_HANDLER
import io.github.alaksion.invoicer.server.data.datasource.BeneficiaryDataSource
import io.github.alaksion.invoicer.server.data.datasource.BeneficiaryDataSourceImpl
import io.github.alaksion.invoicer.server.data.datasource.IntermediaryDataSource
import io.github.alaksion.invoicer.server.data.datasource.IntermediaryDataSourceImpl
import io.github.alaksion.invoicer.server.data.datasource.InvoiceDataSource
import io.github.alaksion.invoicer.server.data.datasource.InvoiceDataSourceImpl
import io.github.alaksion.invoicer.server.data.datasource.UserDataSource
import io.github.alaksion.invoicer.server.data.datasource.UserDataSourceImpl
import io.github.alaksion.invoicer.server.data.repository.BeneficiaryRepositoryImpl
import io.github.alaksion.invoicer.server.data.repository.IntermediaryRepositoryImpl
import io.github.alaksion.invoicer.server.data.repository.InvoiceRepositoryImpl
import io.github.alaksion.invoicer.server.domain.repository.BeneficiaryRepository
import io.github.alaksion.invoicer.server.domain.repository.IntermediaryRepository
import io.github.alaksion.invoicer.server.domain.repository.InvoiceRepository
import io.github.alaksion.invoicer.server.domain.repository.UserRepository
import io.github.alaksion.invoicer.server.domain.repository.UserRepositoryImpl
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.CheckBeneficiarySwiftAvailableUseCase
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.CheckBeneficiarySwiftAvailableUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.CreateBeneficiaryUseCase
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.CreateBeneficiaryUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.DeleteBeneficiaryUseCase
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.DeleteBeneficiaryUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.GetBeneficiaryByIdUseCase
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.GetBeneficiaryByIdUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.GetUserBeneficiariesUseCase
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.GetUserBeneficiariesUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.UpdateBeneficiaryUseCase
import io.github.alaksion.invoicer.server.domain.usecase.beneficiary.UpdateBeneficiaryUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.intermediary.CheckIntermediarySwiftAvailableUseCase
import io.github.alaksion.invoicer.server.domain.usecase.intermediary.CheckIntermediarySwiftAvailableUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.intermediary.CreateIntermediaryUseCase
import io.github.alaksion.invoicer.server.domain.usecase.intermediary.CreateIntermediaryUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.intermediary.DeleteIntermediaryUseCase
import io.github.alaksion.invoicer.server.domain.usecase.intermediary.DeleteIntermediaryUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.intermediary.GetIntermediaryByIdUseCase
import io.github.alaksion.invoicer.server.domain.usecase.intermediary.GetIntermediaryByIdUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.intermediary.GetUserIntermediariesUseCase
import io.github.alaksion.invoicer.server.domain.usecase.intermediary.GetUserIntermediariesUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.intermediary.UpdateIntermediaryUseCase
import io.github.alaksion.invoicer.server.domain.usecase.intermediary.UpdateIntermediaryUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.invoice.CreateInvoiceUseCase
import io.github.alaksion.invoicer.server.domain.usecase.invoice.CreateInvoiceUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.invoice.DeleteInvoiceUseCase
import io.github.alaksion.invoicer.server.domain.usecase.invoice.DeleteInvoiceUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.invoice.GetInvoiceByIdUseCase
import io.github.alaksion.invoicer.server.domain.usecase.invoice.GetInvoiceByIdUseCaseImpl
import io.github.alaksion.invoicer.server.domain.usecase.invoice.GetInvoicesUseCase
import io.github.alaksion.invoicer.server.domain.usecase.invoice.GetInvoicesUseCaseImpl
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
import io.ktor.server.application.Application
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
        import(validatorModule)
        import(secretsModule)


        bindProvider<InvoiceRepository> { InvoiceRepositoryImpl(dataSource = instance()) }
        bindProvider<InvoiceDataSource> { InvoiceDataSourceImpl(dateProvider = instance()) }
        bindProvider<InvoiceDetailsViewModelSender> { InvoiceDetailsViewModelSenderImpl() }
        bindProvider<UserDataSource> {
            UserDataSourceImpl(
                dateProvider = instance()
            )
        }
        bindProvider<UserRepository> {
            UserRepositoryImpl(
                dataSource = instance()
            )
        }

        bindProvider<CreateInvoiceUseCase> {
            CreateInvoiceUseCaseImpl(
                invoiceRepository = instance(),
                dateProvider = instance(),
                getUserByIdUseCase = instance(),
                getBeneficiaryByIdUseCase = instance(),
                getIntermediaryByIdUseCase = instance()
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

        bindProvider<BeneficiaryDataSource> {
            BeneficiaryDataSourceImpl(
                dateProvider = instance()
            )
        }
        bindProvider<BeneficiaryRepository> {
            BeneficiaryRepositoryImpl(
                dataSource = instance()
            )
        }
        bindProvider<CreateBeneficiaryUseCase> {
            CreateBeneficiaryUseCaseImpl(
                getUserByIdUseCase = instance(),
                repository = instance(),
                checkSwiftUseCase = instance(),
                swiftValidator = instance()
            )
        }

        bindProvider<IntermediaryDataSource> {
            IntermediaryDataSourceImpl(
                dateProvider = instance()
            )
        }

        bindProvider<IntermediaryRepository> {
            IntermediaryRepositoryImpl(
                dataSource = instance()
            )
        }
        bindProvider<CreateIntermediaryUseCase> {
            CreateIntermediaryUseCaseImpl(
                getUserByIdUseCase = instance(),
                repository = instance(),
                checkIntermediarySwiftAlreadyUsedUseCaseImpl = instance(),
                swiftValidator = instance()
            )
        }
        bindProvider<GetBeneficiaryByIdUseCase> {
            GetBeneficiaryByIdUseCaseImpl(
                repository = instance()
            )
        }
        bindProvider<GetIntermediaryByIdUseCase> {
            GetIntermediaryByIdUseCaseImpl(
                repository = instance()
            )
        }

        bindProvider<DeleteBeneficiaryUseCase> {
            DeleteBeneficiaryUseCaseImpl(
                getBeneficiaryByIdUseCase = instance(),
                beneficiaryRepository = instance(),
                getUserByIdUseCase = instance(),
                invoiceRepository = instance()
            )
        }

        bindProvider<GetUserBeneficiariesUseCase> {
            GetUserBeneficiariesUseCaseImpl(
                repository = instance()
            )
        }

        bindProvider<CheckBeneficiarySwiftAvailableUseCase> {
            CheckBeneficiarySwiftAvailableUseCaseImpl(
                repository = instance()
            )
        }

        bindProvider<CheckIntermediarySwiftAvailableUseCase> {
            CheckIntermediarySwiftAvailableUseCaseImpl(
                repository = instance()
            )
        }

        bindProvider<UpdateBeneficiaryUseCase> {
            UpdateBeneficiaryUseCaseImpl(
                getUserByIdUseCase = instance(),
                getBeneficiaryByIdUseCase = instance(),
                checkBeneficiarySwiftAvailableUseCase = instance(),
                beneficiaryRepository = instance(),
                swiftValidator = instance()
            )
        }

        bindProvider<DeleteIntermediaryUseCase> {
            DeleteIntermediaryUseCaseImpl(
                intermediaryRepo = instance(),
                getUserByIdUseCase = instance(),
                invoiceRepository = instance(),
                getIntermediaryByIdUseCase = instance()
            )
        }
        bindProvider<UpdateIntermediaryUseCase> {
            UpdateIntermediaryUseCaseImpl(
                getUserByIdUseCase = instance(),
                getIntermediaryByIdUseCase = instance(),
                checkIntermediarySwiftAlreadyUsedUseCaseImpl = instance(),
                intermediaryRepository = instance(),
                swiftValidator = instance()
            )
        }
        bindProvider<GetUserIntermediariesUseCase> {
            GetUserIntermediariesUseCaseImpl(
                repository = instance()
            )
        }
    }
}

internal object DITags {
    const val OPEN_PDF_GENERATOR = "open-pdf"
    const val TEMP_FILE_HANDLER = "temp-file"
}