package services.impl.di

import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import services.api.services.company.CreateCompanyService
import services.api.services.company.GetCompaniesService
import services.api.services.company.GetCompanyDetailsService
import services.api.services.company.GetUserCompanyDetailsService
import services.api.services.customer.CreateCustomerService
import services.api.services.customer.GetCustomerByIdService
import services.api.services.customer.ListCustomersService
import services.api.services.invoice.CreateInvoiceService
import services.api.services.invoice.DeleteInvoiceService
import services.api.services.invoice.GetCompanyInvoicesService
import services.api.services.invoice.GetUserInvoiceByIdService
import services.api.services.login.GoogleLoginService
import services.api.services.login.LoginService
import services.api.services.login.RefreshLoginService
import services.api.services.login.StoreRefreshTokenService
import services.api.services.payaccount.CheckPayAccountDocumentInUseService
import services.api.services.payaccount.DeletePayAccountService
import services.api.services.payaccount.UpdatePayAccountService
import services.api.services.pdf.GenerateInvoicePdfService
import services.api.services.pdf.InvoicePdfSecureLinkService
import services.api.services.qrcodetoken.AuthorizeQrCodeTokenService
import services.api.services.qrcodetoken.GetQrCodeTokenByContentIdService
import services.api.services.qrcodetoken.PollAuthorizedTokenService
import services.api.services.qrcodetoken.RequestQrCodeTokenService
import services.api.services.user.CreateUserService
import services.api.services.user.DeleteUserService
import services.api.services.user.GetUserByEmailService
import services.impl.company.CreateCompanyServiceImpl
import services.impl.company.GetCompaniesServiceImpl
import services.impl.company.GetCompanyDetailsServiceImpl
import services.impl.company.GetUserCompanyDetailsServiceImpl
import services.impl.customer.CreateCustomerServiceImpl
import services.impl.customer.GetCustomerByIdServiceImpl
import services.impl.customer.ListCustomerServiceImpl
import services.impl.invoice.CreateInvoiceServiceImpl
import services.impl.invoice.DeleteInvoiceServiceImpl
import services.impl.invoice.GetCompanyInvoicesServiceImpl
import services.impl.invoice.GetUserInvoiceByIdServiceImpl
import services.impl.login.GoogleLoginServiceImpl
import services.impl.login.LoginServiceImpl
import services.impl.login.RefreshLoginServiceImpl
import services.impl.login.StoreRefreshTokenServiceImpl
import services.impl.payaccount.CheckPayAccountDocumentInUseServiceImpl
import services.impl.payaccount.DeletePayAccountServiceImpl
import services.impl.payaccount.UpdatePayAccountServiceImpl
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
    invoiceServices()
    loginServices()
    userServices()
    payAccountServices()
    companyServices()
    customerServices()
}

private fun DI.Builder.invoiceServices() {
    bindProvider<CreateInvoiceService> {
        CreateInvoiceServiceImpl(
            invoiceRepository = instance(),
            clock = instance(),
            getUserByIdService = instance(),
            messageProducer = instance(),
            getCompanyDetailsService = instance(),
            getCustomerByIdService = instance()
        )
    }

    bindProvider<DeleteInvoiceService> {
        DeleteInvoiceServiceImpl(
            getUserByIdUseCase = instance(),
            getUserInvoiceByIdService = instance(),
            repository = instance(),
            getCompanyByIdService = instance()
        )
    }

    bindProvider<GetCompanyInvoicesService> {
        GetCompanyInvoicesServiceImpl(
            repository = instance(),
            companyRepository = instance(),
            getUserByIdService = instance()
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

    bindProvider<GetUserInvoiceByIdService> {
        GetUserInvoiceByIdServiceImpl(repository = instance())
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

    bindProvider<UpdatePayAccountService> {
        UpdatePayAccountServiceImpl(
            paymentAccountRepository = instance(),
            getUserCompanyDetailsService = instance(),
            swiftValidator = instance(),
            ibanValidator = instance()
        )
    }

    bindProvider<DeletePayAccountService> {
        DeletePayAccountServiceImpl(
            paymentAccountRepository = instance(),
            getUserCompanyDetailsService = instance()
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

    bindProvider<GetCompanyDetailsService> {
        GetCompanyDetailsServiceImpl(
            companyRepository = instance()
        )
    }

    bindProvider<GetUserCompanyDetailsService> {
        GetUserCompanyDetailsServiceImpl(
            companyRepository = instance(),
            getUserByIdService = instance()
        )
    }
}

private fun DI.Builder.customerServices() {
    bindProvider<CreateCustomerService> {
        CreateCustomerServiceImpl(
            getUserByIdService = instance(),
            userCompanyRepository = instance(),
            customerRepository = instance(),
            emailValidator = instance()
        )
    }

    bindProvider<ListCustomersService> {
        ListCustomerServiceImpl(
            customerRepository = instance(),
            getUserByIdService = instance()
        )
    }

    bindProvider<GetCustomerByIdService> {
        GetCustomerByIdServiceImpl(
            customerRepository = instance()
        )
    }
}