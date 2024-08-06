package io.github.alaksion.invoicer.server.domain.usecase.user

import io.github.alaksion.invoicer.server.domain.errors.badRequestError
import io.github.alaksion.invoicer.server.domain.errors.httpError
import io.github.alaksion.invoicer.server.domain.model.user.CreateUserModel
import io.github.alaksion.invoicer.server.domain.repository.UserRepository
import io.github.alaksion.invoicer.server.util.PasswordStrength
import io.github.alaksion.invoicer.server.util.PasswordValidator
import io.github.alaksion.invoicer.server.util.isValidEmail
import io.ktor.http.*

interface CreateUserUseCase {
    suspend fun create(userModel: CreateUserModel)
}

internal class CreateUserUseCaseImpl(
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val passwordValidator: PasswordValidator,
    private val repository: UserRepository
) : CreateUserUseCase {

    override suspend fun create(userModel: CreateUserModel) {

        if (userModel.email != userModel.confirmEmail)
            badRequestError(message = "E-mails must mach")

        if (isValidEmail(userModel.email).not())
            badRequestError(message = "invalid email format: ${userModel.email}")

        if (getUserByEmailUseCase.get(userModel.email) != null)
            httpError(message = "E-mail ${userModel.email} already in use", code = HttpStatusCode.Conflict)

        val passwordLevel = passwordValidator.validate(userModel.password)

        if (passwordLevel is PasswordStrength.WEAK) {
            badRequestError(message = passwordLevel.reason)
        }


    }

}