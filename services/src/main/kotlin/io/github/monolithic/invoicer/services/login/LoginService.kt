package io.github.monolithic.invoicer.services.login

import io.github.monolithic.invoicer.foundation.password.PasswordEncryption
import io.github.monolithic.invoicer.foundation.authentication.token.AuthTokenManager
import io.github.monolithic.invoicer.utils.validation.EmailValidator
import io.github.monolithic.invoicer.models.login.AuthTokenModel
import io.github.monolithic.invoicer.models.login.LoginModel
import io.github.monolithic.invoicer.services.user.GetUserByEmailService
import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError
import io.github.monolithic.invoicer.foundation.exceptions.http.notFoundError

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
