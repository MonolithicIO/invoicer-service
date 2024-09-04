package domains.user.domain.api.di

import domains.user.domain.api.usecase.CreateUserUseCase
import domains.user.domain.api.usecase.CreateUserUseCaseImpl
import domains.user.domain.api.usecase.DeleteUserUseCase
import domains.user.domain.api.usecase.DeleteUserUseCaseImpl
import domains.user.domain.api.usecase.GetUserByEmailUseCase
import domains.user.domain.api.usecase.GetUserByEmailUseCaseImpl
import domains.user.domain.api.usecase.GetUserByIdUseCase
import domains.user.domain.api.usecase.GetUserByIdUseCaseImpl
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val userDomainModule = DI.Module("user-domain-module") {
    bindProvider<CreateUserUseCase> {
        CreateUserUseCaseImpl(
            emailValidator = instance(),
            getUserByEmailUseCase = instance(),
            passwordValidator = instance(),
            repository = instance(),
            passwordEncryption = instance()
        )
    }

    bindProvider<DeleteUserUseCase> {
        DeleteUserUseCaseImpl(
            userRepository = instance(),
            getUserByIdUseCase = instance()
        )
    }

    bindProvider<GetUserByEmailUseCase> {
        GetUserByEmailUseCaseImpl(
            userRepository = instance()
        )
    }

    bindProvider<GetUserByIdUseCase> {
        GetUserByIdUseCaseImpl(
            userRepository = instance()
        )
    }
}