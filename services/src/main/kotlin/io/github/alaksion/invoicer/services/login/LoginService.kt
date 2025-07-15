package io.github.alaksion.invoicer.services.login

import foundation.password.PasswordEncryption
import io.github.alaksion.invoicer.foundation.authentication.token.AuthTokenManager
import io.github.alaksion.invoicer.utils.validation.EmailValidator
import models.login.AuthTokenModel
import models.login.LoginModel
import io.github.alaksion.invoicer.services.user.GetUserByEmailService
import utils.exceptions.http.badRequestError
import utils.exceptions.http.notFoundError

interface LoginService {
    suspend fun login(model: LoginModel): AuthTokenModel
}

internal class LoginServiceImpl(
    private val getUserByEmailService: GetUserByEmailService,
    private val authTokenManager: AuthTokenManager,
    private val passwordEncryption: PasswordEncryption,
    private val emailValidator: EmailValidator,
    private val storeRefreshTokenService: StoreRefreshTokenService
) : LoginService {

    override suspend fun login(model: LoginModel): AuthTokenModel {

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

        val accessToken = authTokenManager.generateToken(account.id.toString())
        val refreshToken = authTokenManager.generateRefreshToken(account.id.toString())

        storeRefreshTokenService.storeRefreshToken(
            token = refreshToken,
            userId = account.id
        )

        return AuthTokenModel(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}
