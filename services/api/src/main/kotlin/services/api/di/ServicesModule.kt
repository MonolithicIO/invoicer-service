package services.api.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import services.api.services.beneficiary.CheckBeneficiarySwiftAvailableService
import services.api.services.beneficiary.CheckBeneficiarySwiftAvailableServiceImpl
import services.api.services.beneficiary.CreateBeneficiaryService
import services.api.services.beneficiary.CreateBeneficiaryServiceImpl
import services.api.services.beneficiary.DeleteBeneficiaryService
import services.api.services.beneficiary.DeleteBeneficiaryServiceImpl
import services.api.services.beneficiary.GetBeneficiaryByIdService
import services.api.services.beneficiary.GetBeneficiaryByIdServiceImpl
import services.api.services.beneficiary.GetUserBeneficiariesService
import services.api.services.beneficiary.GetUserBeneficiariesServiceImpl
import services.api.services.beneficiary.UpdateBeneficiaryService
import services.api.services.beneficiary.UpdateBeneficiaryServiceImpl
import services.api.services.intermediary.CheckIntermediarySwiftAvailableService
import services.api.services.intermediary.CheckIntermediarySwiftAvailableServiceImpl
import services.api.services.intermediary.CreateIntermediaryService
import services.api.services.intermediary.CreateIntermediaryServiceImpl
import services.api.services.intermediary.DeleteIntermediaryService
import services.api.services.intermediary.DeleteIntermediaryServiceImpl
import services.api.services.intermediary.GetIntermediaryByIdService
import services.api.services.intermediary.GetIntermediaryByIdServiceImpl
import services.api.services.intermediary.GetUserIntermediariesService
import services.api.services.intermediary.GetUserIntermediariesServiceImpl
import services.api.services.intermediary.UpdateIntermediaryService
import services.api.services.intermediary.UpdateIntermediaryServiceImpl
import services.api.services.invoice.CreateInvoiceService
import services.api.services.invoice.CreateInvoiceServiceImpl
import services.api.services.invoice.DeleteInvoiceService
import services.api.services.invoice.DeleteInvoiceServiceImpl
import services.api.services.invoice.GetInvoiceByIdService
import services.api.services.invoice.GetInvoiceByIdServiceImpl
import services.api.services.invoice.GetUserInvoicesService
import services.api.services.invoice.GetUserInvoicesServiceImpl
import services.api.services.login.LoginService
import services.api.services.login.LoginServiceImpl
import services.api.services.user.CreateUserService
import services.api.services.user.CreateUserServiceImpl
import services.api.services.user.DeleteUserService
import services.api.services.user.DeleteUserServiceImpl
import services.api.services.user.GetUserByEmailService
import services.api.services.user.GetUserByEmailServiceImpl
import services.api.services.user.GetUserByIdServiceImpl

val servicesModule = DI.Module("invoicer-services") {
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
            checkSwiftUseCase = instance(),
            swiftValidator = instance(),
            ibanValidator = instance()
        )
    }

    bindProvider<DeleteBeneficiaryService> {
        DeleteBeneficiaryServiceImpl(
            getBeneficiaryByIdService = instance(),
            beneficiaryRepository = instance(),
            getUserByIdUseCase = instance(),
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
            repository = instance()
        )
    }

    bindProvider<UpdateBeneficiaryService> {
        UpdateBeneficiaryServiceImpl(
            swiftValidator = instance(),
            getBeneficiaryByIdService = instance(),
            beneficiaryRepository = instance(),
            getUserByIdUseCase = instance(),
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