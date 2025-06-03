package services.impl.di

import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import services.api.services.beneficiary.*
import services.api.services.company.CreateCompanyService
import services.api.services.company.GetCompaniesService
import services.api.services.intermediary.*
import services.api.services.invoice.CreateInvoiceService
import services.api.services.invoice.DeleteInvoiceService
import services.api.services.invoice.GetUserInvoiceByIdService
import services.api.services.invoice.GetUserInvoicesService
import services.api.services.login.GoogleLoginService
import services.api.services.login.LoginService
import services.api.services.login.RefreshLoginService
import services.api.services.login.StoreRefreshTokenService
import services.api.services.payaccount.CheckPayAccountDocumentInUseService
import services.api.services.pdf.GenerateInvoicePdfService
import services.api.services.pdf.InvoicePdfSecureLinkService
import services.api.services.qrcodetoken.AuthorizeQrCodeTokenService
import services.api.services.qrcodetoken.GetQrCodeTokenByContentIdService
import services.api.services.qrcodetoken.PollAuthorizedTokenService
import services.api.services.qrcodetoken.RequestQrCodeTokenService
import services.api.services.user.CreateUserService
import services.api.services.user.DeleteUserService
import services.api.services.user.GetUserByEmailService
import services.impl.beneficiary.*
import services.impl.company.CreateCompanyServiceImpl
import services.impl.company.GetCompaniesServiceImpl
import services.impl.intermediary.*
import services.impl.invoice.CreateInvoiceServiceImpl
import services.impl.invoice.DeleteInvoiceServiceImpl
import services.impl.invoice.GetUserInvoiceByIdServiceImpl
import services.impl.invoice.GetUserInvoicesServiceImpl
import services.impl.login.GoogleLoginServiceImpl
import services.impl.login.LoginServiceImpl
import services.impl.login.RefreshLoginServiceImpl
import services.impl.login.StoreRefreshTokenServiceImpl
import services.impl.payaccount.CheckPayAccountDocumentInUseServiceImpl
import services.impl.pdf.GenerateInvoicePdfServiceImpl
import services.impl.pdf.InvoicePdfSecureLinkServiceImpl
import services.impl.pdf.pdfwriter.InvoicePdfWriter
import services.impl.pdf.pdfwriter.itext.ItextInvoiceWriter
import services.impl.qrcodetoken.AuthorizeQrCodeTokenServiceImpl
import services.impl.qrcodetoken.GetQrCodeTokenByContentIdServiceImpl
import services.impl.qrcodetoken.PollAuthorizedTokenServiceImpl
import services.impl.qrcodetoken.RequestQrCodeTokenServiceImpl
import services.impl.user.CreateUserServiceImpl
import services.impl.user.DeleteUserServiceImpl
import services.impl.user.GetUserByEmailServiceImpl
import services.impl.user.GetUserByIdServiceImpl

val servicesImplModule = DI.Module("invoicer-services") {
    beneficiaryServices()
    intermediaryServices()
    invoiceServices()
    loginServices()
    userServices()
    payAccountServices()
    companyServices()
}

private fun DI.Builder.beneficiaryServices() {
    bindProvider<CheckBeneficiarySwiftAvailableService> {
        CheckBeneficiarySwiftAvailableServiceImpl(
            repository = instance()
        )
    }

    bindProvider<CreateBeneficiaryService> {
        CreateBeneficiaryServiceImpl(
            repository = instance(),
            getUserByIdService = instance(),
            checkSwiftService = instance(),
            swiftValidator = instance(),
            ibanValidator = instance()
        )
    }

    bindProvider<DeleteBeneficiaryService> {
        DeleteBeneficiaryServiceImpl(
            getBeneficiaryByIdService = instance(),
            beneficiaryRepository = instance(),
            getUserByIdService = instance(),
            invoiceRepository = instance()
        )
    }

    bindProvider<GetBeneficiaryByIdService> {
        GetBeneficiaryByIdServiceImpl(
            repository = instance(),
        )
    }

    bindProvider<GetUserBeneficiariesService> {
        GetUserBeneficiariesServiceImpl(
            repository = instance(),
            getUserByIdUseCase = instance()
        )
    }

    bindProvider<UpdateBeneficiaryService> {
        UpdateBeneficiaryServiceImpl(
            swiftValidator = instance(),
            getBeneficiaryByIdService = instance(),
            beneficiaryRepository = instance(),
            getUserByIdService = instance(),
            checkBeneficiarySwiftAvailableService = instance(),
            ibanValidator = instance()
        )
    }
}

private fun DI.Builder.intermediaryServices() {
    bindProvider<CheckIntermediarySwiftAvailableService> {
        CheckIntermediarySwiftAvailableServiceImpl(
            repository = instance()
        )
    }

    bindProvider<CreateIntermediaryService> {
        CreateIntermediaryServiceImpl(
            repository = instance(),
            swiftValidator = instance(),
            getUserByIdService = instance(),
            checkIntermediarySwiftAlreadyUsedService = instance(),
            ibanValidator = instance()
        )
    }

    bindProvider<DeleteIntermediaryService> {
        DeleteIntermediaryServiceImpl(
            getIntermediaryByIdService = instance(),
            intermediaryRepo = instance(),
            getUserByIdUseCase = instance(),
            invoiceRepository = instance()
        )
    }

    bindProvider<GetIntermediaryByIdService> {
        GetIntermediaryByIdServiceImpl(
            repository = instance(),
        )
    }

    bindProvider<UpdateIntermediaryService> {
        UpdateIntermediaryServiceImpl(
            swiftValidator = instance(),
            getIntermediaryByIdService = instance(),
            intermediaryRepository = instance(),
            getUserByIdService = instance(),
            checkIntermediarySwiftAlreadyUsedService = instance(),
            ibanValidator = instance()
        )
    }

    bindProvider<GetUserIntermediariesService> {
        GetUserIntermediariesServiceImpl(
            repository = instance(),
            getUserByIdService = instance()
        )
    }
}

private fun DI.Builder.invoiceServices() {
    bindProvider<CreateInvoiceService> {
        CreateInvoiceServiceImpl(
            invoiceRepository = instance(),
            clock = instance(),
            getUserByIdService = instance(),
            getBeneficiaryByIdService = instance(),
            getIntermediaryByIdService = instance(),
            messageProducer = instance()
        )
    }

    bindProvider<DeleteInvoiceService> {
        DeleteInvoiceServiceImpl(
            getUserByIdUseCase = instance(),
            getUserInvoiceByIdService = instance(),
            repository = instance()
        )
    }

    bindProvider<GetUserInvoiceByIdService> {
        GetUserInvoiceByIdServiceImpl(
            repository = instance(),
            getUserByIdUseCase = instance()
        )
    }

    bindProvider<GetUserInvoicesService> {
        GetUserInvoicesServiceImpl(
            repository = instance()
        )
    }

    bindProvider<InvoicePdfWriter> { ItextInvoiceWriter() }

    bindProvider<GenerateInvoicePdfService> {
        GenerateInvoicePdfServiceImpl(
            getUserByIdService = instance(),
            getUserInvoiceByIdService = instance(),
            writer = instance(),
            fileUploader = instance(),
            invoicePdfRepository = instance(),
            localStorage = instance()
        )
    }

    bindProvider<InvoicePdfSecureLinkService> {
        InvoicePdfSecureLinkServiceImpl(
            getUserInvoiceByIdService = instance(),
            invoicePdfRepository = instance(),
            secureFileLinkGenerator = instance()
        )
    }
}

private fun DI.Builder.loginServices() {
    bindProvider<LoginService> {
        LoginServiceImpl(
            getUserByEmailService = instance(),
            authTokenManager = instance(),
            passwordEncryption = instance(),
            emailValidator = instance(),
            storeRefreshTokenService = instance()
        )
    }

    bindProvider<RefreshLoginService> {
        RefreshLoginServiceImpl(
            tokenManager = instance(),
            getUserByIdService = instance(),
            refreshTokenRepository = instance(),
            storeRefreshTokenService = instance()
        )
    }

    bindProvider<StoreRefreshTokenService> {
        StoreRefreshTokenServiceImpl(
            refreshTokenRepository = instance()
        )
    }

    bindProvider<RequestQrCodeTokenService> {
        RequestQrCodeTokenServiceImpl(
            qrCodeGenerator = instance(),
            qrCodeTokenRepository = instance()
        )
    }

    bindProvider<AuthorizeQrCodeTokenService> {
        AuthorizeQrCodeTokenServiceImpl(
            qrCodeTokenRepository = instance(),
            clock = instance(),
            getUserByIdService = instance(),
            authTokenManager = instance(),
            storeRefreshTokenService = instance(),
        )
    }

    bindProvider<GetQrCodeTokenByContentIdService> {
        GetQrCodeTokenByContentIdServiceImpl(
            qrCodeTokenRepository = instance()
        )
    }

    bindProvider<PollAuthorizedTokenService> {
        PollAuthorizedTokenServiceImpl(
            qrCodeTokenRepository = instance(),
            getTokenService = instance(),
            dispatcher = Dispatchers.IO,
            logger = instance()
        )
    }

    bindProvider<GoogleLoginService> {
        GoogleLoginServiceImpl(
            identityProvider = instance(),
            getUserByEmailServiceImpl = instance(),
            userRepository = instance(),
            authTokenManager = instance(),
            storeRefreshTokenService = instance(),
            passwordEncryption = instance()
        )
    }
}

private fun DI.Builder.userServices() {
    bindProvider<CreateUserService> {
        CreateUserServiceImpl(
            repository = instance(),
            passwordEncryption = instance(),
            emailValidator = instance(),
            getUserByEmailService = instance(),
            passwordValidator = instance()
        )
    }

    bindProvider<DeleteUserService> {
        DeleteUserServiceImpl(
            getUserByIdService = instance(),
            userRepository = instance()
        )
    }

    bindProvider<GetUserByEmailService> {
        GetUserByEmailServiceImpl(
            userRepository = instance()
        )
    }

    bindProvider<GetUserByIdServiceImpl> {
        GetUserByIdServiceImpl(
            userRepository = instance()
        )
    }
}

private fun DI.Builder.payAccountServices() {
    bindProvider<CheckPayAccountDocumentInUseService> {
        CheckPayAccountDocumentInUseServiceImpl(
            paymentAccountRepository = instance()
        )
    }
}

private fun DI.Builder.companyServices() {
    bindProvider<CreateCompanyService> {
        CreateCompanyServiceImpl(
            swiftValidator = instance(),
            ibanValidator = instance(),
            countryCodeValidator = instance(),
            checkPayAccountDocumentService = instance(),
            userCompanyRepository = instance()
        )
    }

    bindProvider<GetCompaniesService> {
        GetCompaniesServiceImpl(
            repository = instance(),
            getUserByIdService = instance()
        )
    }
}