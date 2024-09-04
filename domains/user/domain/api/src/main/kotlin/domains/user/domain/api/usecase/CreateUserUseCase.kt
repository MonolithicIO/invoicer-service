package domains.user.domain.api.usecase

import domains.user.domain.api.models.CreateUserModel
import domains.user.domain.api.repository.UserRepository
import foundation.validator.api.EmailValidator
import io.ktor.http.*
import utils.exceptions.badRequestError
import utils.exceptions.httpError
import utils.password.PasswordEncryption
import utils.password.PasswordStrength
import utils.password.PasswordValidator

interface CreateUserUseCase {
    suspend fun create(userModel: CreateUserModel): String
}

internal class CreateUserUseCaseImpl(
    private val getUserByEmailUseCase: GetUserByEmailUseCase,
    private val passwordValidator: PasswordValidator,
    private val repository: UserRepository,
    private val passwordEncryption: PasswordEncryption,
    private val emailValidator: EmailValidator
) : CreateUserUseCase {

    override suspend fun create(userModel: CreateUserModel): String {

        if (userModel.email != userModel.confirmEmail)
            badRequestError(message = "E-mails must mach")

        if (emailValidator.validate(userModel.email).not())
            badRequestError(message = "invalid email format: ${userModel.email}")

        if (getUserByEmailUseCase.get(userModel.email) != null)
            httpError(message = "E-mail ${userModel.email} already in use", code = HttpStatusCode.Conflict)

        val passwordLevel = passwordValidator.validate(userModel.password)

        if (passwordLevel is PasswordStrength.WEAK) {
            badRequestError(message = passwordLevel.reason)
        }

        val encryptedPassword = passwordEncryption.encryptPassword(userModel.password)

        return repository.createUser(userModel.copy(password = encryptedPassword))
    }

}