package io.github.monolithic.invoicer.services.di

import io.github.monolithic.invoicer.services.company.CreateCompanyService
import io.github.monolithic.invoicer.services.company.CreateCompanyServiceImpl
import io.github.monolithic.invoicer.services.company.GetCompaniesService
import io.github.monolithic.invoicer.services.company.GetCompaniesServiceImpl
import io.github.monolithic.invoicer.services.company.GetCompanyDetailsService
import io.github.monolithic.invoicer.services.company.GetCompanyDetailsServiceImpl
import io.github.monolithic.invoicer.services.company.GetUserCompanyDetailsService
import io.github.monolithic.invoicer.services.company.GetUserCompanyDetailsServiceImpl
import io.github.monolithic.invoicer.services.company.UpdateCompanyAddressService
import io.github.monolithic.invoicer.services.company.UpdateCompanyAddressServiceImpl
import io.github.monolithic.invoicer.services.customer.CreateCustomerService
import io.github.monolithic.invoicer.services.customer.CreateCustomerServiceImpl
import io.github.monolithic.invoicer.services.customer.GetCustomerByIdService
import io.github.monolithic.invoicer.services.customer.GetCustomerByIdServiceImpl
import io.github.monolithic.invoicer.services.customer.ListCustomerServiceImpl
import io.github.monolithic.invoicer.services.customer.ListCustomersService
import io.github.monolithic.invoicer.services.invoice.CreateInvoiceService
import io.github.monolithic.invoicer.services.invoice.CreateInvoiceServiceImpl
import io.github.monolithic.invoicer.services.invoice.DeleteInvoiceService
import io.github.monolithic.invoicer.services.invoice.DeleteInvoiceServiceImpl
import io.github.monolithic.invoicer.services.invoice.GetCompanyInvoicesService
import io.github.monolithic.invoicer.services.invoice.GetCompanyInvoicesServiceImpl
import io.github.monolithic.invoicer.services.invoice.GetUserInvoiceByIdService
import io.github.monolithic.invoicer.services.invoice.GetUserInvoiceByIdServiceImpl
import io.github.monolithic.invoicer.services.login.GoogleLoginService
import io.github.monolithic.invoicer.services.login.GoogleLoginServiceImpl
import io.github.monolithic.invoicer.services.login.LoginService
import io.github.monolithic.invoicer.services.login.LoginServiceImpl
import io.github.monolithic.invoicer.services.login.RefreshLoginService
import io.github.monolithic.invoicer.services.login.RefreshLoginServiceImpl
import io.github.monolithic.invoicer.services.login.StoreRefreshTokenService
import io.github.monolithic.invoicer.services.login.StoreRefreshTokenServiceImpl
import io.github.monolithic.invoicer.services.payaccount.CheckPayAccountDocumentInUseService
import io.github.monolithic.invoicer.services.payaccount.CheckPayAccountDocumentInUseServiceImpl
import io.github.monolithic.invoicer.services.payaccount.DeletePayAccountService
import io.github.monolithic.invoicer.services.payaccount.DeletePayAccountServiceImpl
import io.github.monolithic.invoicer.services.payaccount.UpdatePayAccountService
import io.github.monolithic.invoicer.services.payaccount.UpdatePayAccountServiceImpl
import io.github.monolithic.invoicer.services.pdf.GenerateInvoicePdfService
import io.github.monolithic.invoicer.services.pdf.GenerateInvoicePdfServiceImpl
import io.github.monolithic.invoicer.services.pdf.InvoicePdfSecureLinkService
import io.github.monolithic.invoicer.services.pdf.InvoicePdfSecureLinkServiceImpl
import io.github.monolithic.invoicer.services.pdf.pdfwriter.InvoicePdfWriter
import io.github.monolithic.invoicer.services.pdf.pdfwriter.itext.ItextInvoiceWriter
import io.github.monolithic.invoicer.services.qrcodetoken.AuthorizeQrCodeTokenService
import io.github.monolithic.invoicer.services.qrcodetoken.AuthorizeQrCodeTokenServiceImpl
import io.github.monolithic.invoicer.services.qrcodetoken.GetQrCodeTokenByContentIdService
import io.github.monolithic.invoicer.services.qrcodetoken.GetQrCodeTokenByContentIdServiceImpl
import io.github.monolithic.invoicer.services.qrcodetoken.PollAuthorizedTokenService
import io.github.monolithic.invoicer.services.qrcodetoken.PollAuthorizedTokenServiceImpl
import io.github.monolithic.invoicer.services.qrcodetoken.RequestQrCodeTokenService
import io.github.monolithic.invoicer.services.qrcodetoken.RequestQrCodeTokenServiceImpl
import io.github.monolithic.invoicer.services.user.ConsumeResetPasswordRequestService
import io.github.monolithic.invoicer.services.user.ConsumeResetPasswordRequestServiceImpl
import io.github.monolithic.invoicer.services.user.CreateUserService
import io.github.monolithic.invoicer.services.user.CreateUserServiceImpl
import io.github.monolithic.invoicer.services.user.DeleteUserService
import io.github.monolithic.invoicer.services.user.DeleteUserServiceImpl
import io.github.monolithic.invoicer.services.user.GetUserByEmailService
import io.github.monolithic.invoicer.services.user.GetUserByEmailServiceImpl
import io.github.monolithic.invoicer.services.user.GetUserByIdServiceImpl
import io.github.monolithic.invoicer.services.user.RequestPasswordResetService
import io.github.monolithic.invoicer.services.user.RequestPasswordResetServiceImpl
import io.github.monolithic.invoicer.services.user.SendRestPasswordEmailService
import io.github.monolithic.invoicer.services.user.SendRestPasswordEmailServiceImpl
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

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
        GetUserInvoiceByIdServiceImpl(
            repository = instance(),
            getCompanyDetailsService = instance(),
            getUserService = instance(),
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
            storeRefreshTokenService = instance(),
            clock = instance()
        )
    }

    bindProvider<StoreRefreshTokenService> {
        StoreRefreshTokenServiceImpl(
            refreshTokenRepository = instance(),
            clock = instance()
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

    bindProvider<RequestPasswordResetService> {
        RequestPasswordResetServiceImpl(
            uuidProvider = instance(),
            getUserByEmailService = instance(),
            codeGenerator = instance(),
            clock = instance(),
            messageProducer = instance(),
            passwordResetRepository = instance(),
            emailValidator = instance()
        )
    }

    bindProvider<SendRestPasswordEmailService> {
        SendRestPasswordEmailServiceImpl(
            emailSender = instance(),
            resetPasswordRepository = instance(),
            getUserByIdService = instance(),
            clock = instance(),
            logger = instance(),
        )
    }

    bindProvider<ConsumeResetPasswordRequestService> {
        ConsumeResetPasswordRequestServiceImpl(
            passwordResetRepository = instance(),
            getUserByIdService = instance(),
            clock = instance(),
            uuidProvider = instance(),
            logger = instance()
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

    bindProvider<UpdateCompanyAddressService> {
        UpdateCompanyAddressServiceImpl(
            getUserByIdService = instance(),
            getCompanyByIdService = instance(),
            companyAddressRepository = instance()
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
