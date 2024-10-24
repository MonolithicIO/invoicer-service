package services.impl.login

import foundation.validator.api.EmailValidator
import models.login.LoginModel
import services.api.services.user.GetUserByEmailService
import utils.authentication.api.AuthTokenManager
import utils.exceptions.badRequestError
import utils.exceptions.notFoundError
import utils.password.PasswordEncryption

interface LoginService {
    suspend fun login(model: LoginModel): String
}

internal class LoginServiceImpl(
    private val getUserByEmailService: GetUserByEmailService,
    private val authTokenManager: AuthTokenManager,
    private val passwordEncryption: PasswordEncryption,
    private val emailValidator: EmailValidator
) : LoginService {

    override suspend fun login(model: LoginModel): String {

        if (emailValidator.validate(model.email).not()) badRequestError(message = "Invalid e-mail format")

        val account = getUserByEmailService.get(model.email)
            ?: notFoundError(message = "Invalid credentials, user not found.")

        if (passwordEncryption.comparePassword(
                encryptedPassword = account.password,
                rawPassword = model.password
            ).not()
        ) {
            notFoundError(
                message = "Invalid credentials, user not found"
            )
        }

        return authTokenManager.generateToken(
            account.id.toString()
        )
    }
}