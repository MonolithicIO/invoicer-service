package services.impl.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import services.api.services.beneficiary.*
import services.api.services.intermediary.*
import services.api.services.invoice.CreateInvoiceService
import services.api.services.invoice.DeleteInvoiceService
import services.api.services.invoice.GetInvoiceByIdService
import services.api.services.invoice.GetUserInvoicesService
import services.api.services.login.LoginService
import services.api.services.login.RefreshLoginService
import services.api.services.login.StoreRefreshTokenService
import services.api.services.qrcodetoken.ConsumeQrCodeTokenService
import services.api.services.qrcodetoken.GetQrCodeTokenByContentIdService
import services.api.services.qrcodetoken.RequestQrCodeTokenService
import services.api.services.user.CreateUserService
import services.api.services.user.DeleteUserService
import services.api.services.user.GetUserByEmailService
import services.impl.beneficiary.*
import services.impl.intermediary.*
import services.impl.invoice.CreateInvoiceServiceImpl
import services.impl.invoice.DeleteInvoiceServiceImpl
import services.impl.invoice.GetInvoiceByIdServiceImpl
import services.impl.invoice.GetUserInvoicesServiceImpl
import services.impl.login.LoginServiceImpl
import services.impl.login.RefreshLoginServiceImpl
import services.impl.login.StoreRefreshTokenServiceImpl
import services.impl.qrcodetoken.ConsumeQrCodeTokenServiceImpl
import services.impl.qrcodetoken.GetQrCodeTokenByContentIdServiceImpl
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

    bindProvider<GetBeneficiaryDetailsService> {
        GetBeneficiaryDetailsServiceServiceImpl(
            repository = instance(),
            getUserService = instance()
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

    bindProvider<GetIntermediaryDetailsService> {
        GetIntermediaryDetailsServiceImpl(
            intermediaryRepository = instance(),
            getUserByIdService = instance()
        )
    }
}

private fun DI.Builder.invoiceServices() {
    bindProvider<CreateInvoiceService> {
        CreateInvoiceServiceImpl(
            invoiceRepository = instance(),
            dateProvider = instance(),
            getUserByIdService = instance(),
            getBeneficiaryByIdService = instance(),
            getIntermediaryByIdService = instance()
        )
    }

    bindProvider<DeleteInvoiceService> {
        DeleteInvoiceServiceImpl(
            getUserByIdUseCase = instance(),
            getInvoiceByIdService = instance(),
            repository = instance()
        )
    }

    bindProvider<GetInvoiceByIdService> {
        GetInvoiceByIdServiceImpl(
            repository = instance(),
            getUserByIdUseCase = instance()
        )
    }

    bindProvider<GetUserInvoicesService> {
        GetUserInvoicesServiceImpl(
            repository = instance()
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

    bindProvider<ConsumeQrCodeTokenService> {
        ConsumeQrCodeTokenServiceImpl(
            qrCodeTokenRepository = instance(),
            dateProvider = instance(),
            getUserByIdService = instance(),
            authTokenManager = instance(),
            storeRefreshTokenService = instance(),
            qrCodeTokenStream = instance(),
        )
    }

    bindProvider<GetQrCodeTokenByContentIdService> {
        GetQrCodeTokenByContentIdServiceImpl(
            qrCodeTokenRepository = instance()
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