package io.github.monolithic.invoicer.services.user

import io.github.monolithic.invoicer.foundation.password.PasswordEncryption
import io.github.monolithic.invoicer.foundation.password.PasswordStrength
import io.github.monolithic.invoicer.foundation.password.PasswordValidator
import io.github.monolithic.invoicer.utils.validation.EmailValidator
import io.github.monolithic.invoicer.models.user.CreateUserModel
import io.github.monolithic.invoicer.repository.UserRepository
import io.github.monolithic.invoicer.foundation.exceptions.http.HttpCode
import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError
import io.github.monolithic.invoicer.foundation.exceptions.http.httpError

interface CreateUserService {
    suspend fun create(userModel: CreateUserModel): String
}

internal class CreateUserServiceImpl(
    private val getUserByEmailService: GetUserByEmailService,
    private val passwordValidator: PasswordValidator,
    private val repository: UserRepository,
    private val passwordEncryption: PasswordEncryption,
    private val emailValidator: EmailValidator
) : CreateUserService {

    override suspend fun create(userModel: CreateUserModel): String {

        if (userModel.email != userModel.confirmEmail)
            badRequestError(message = "E-mails must mach")

        if (emailValidator.validate(userModel.email).not())
            badRequestError(message = "invalid email format: ${userModel.email}")

        if (getUserByEmailService.get(userModel.email) != null)
            httpError(message = "E-mail ${userModel.email} already in use", code = HttpCode.Conflict)

        val passwordLevel = passwordValidator.validate(userModel.password)

        if (passwordLevel is PasswordStrength.WEAK) {
            badRequestError(message = passwordLevel.reason)
        }

        val encryptedPassword = passwordEncryption.encryptPassword(userModel.password)

        return repository.createUser(userModel.copy(password = encryptedPassword))
    }

}
