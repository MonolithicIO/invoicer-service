package services.impl.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import services.api.services.beneficiary.*
import services.api.services.intermediary.*
import services.api.services.login.LoginService
import services.api.services.user.CreateUserService
import services.api.services.user.DeleteUserService
import services.api.services.user.GetUserByEmailService
import services.impl.beneficiary.*
import services.impl.intermediary.*
import services.impl.invoice.*
import services.impl.login.LoginServiceImpl
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
            getUserByIdUseCase = instance(),
            checkIntermediarySwiftAlreadyUsedService = instance()
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
            getUserByIdUseCase = instance(),
            checkIntermediarySwiftAlreadyUsedService = instance(),
        )
    }

    bindProvider<GetUserIntermediariesService> {
        GetUserIntermediariesServiceImpl(
            repository = instance()
        )
    }
}

private fun DI.Builder.invoiceServices() {
    bindProvider<CreateInvoiceService> {
        CreateInvoiceServiceImpl(
            invoiceRepository = instance(),
            dateProvider = instance(),
            getUserByIdUseCase = instance(),
            getBeneficiaryByIdUseCase = instance(),
            getIntermediaryByIdUseCase = instance()
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
            emailValidator = instance()
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