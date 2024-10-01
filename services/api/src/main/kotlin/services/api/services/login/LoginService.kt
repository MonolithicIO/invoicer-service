package services.api.services.login

import io.github.alaksion.invoicer.server.util.isValidEmail
import services.api.model.login.LoginModel
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
    private val passwordEncryption: PasswordEncryption
) : LoginService {

    override suspend fun login(model: LoginModel): String {

        if (isValidEmail(model.email).not()) badRequestError(message = "Invalid e-mail format")

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