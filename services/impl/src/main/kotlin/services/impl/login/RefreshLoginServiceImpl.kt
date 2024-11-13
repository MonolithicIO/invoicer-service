package services.impl.login

import models.login.AuthTokenModel
import services.api.services.login.RefreshLoginService
import services.api.services.user.GetUserByIdService
import utils.authentication.api.AuthTokenManager
import utils.authentication.api.jwt.InvoicerJwtVerifier
import utils.exceptions.unauthorizedError

internal class RefreshLoginServiceImpl(
    private val invoicerJwtVerifier: InvoicerJwtVerifier,
    private val tokenManager: AuthTokenManager,
    private val getUserByIdService: GetUserByIdService
) : RefreshLoginService {

    override suspend fun refreshLogin(refreshToken: String): AuthTokenModel {
        val user = getUserByIdService.get(peekUserId(refreshToken))

        if (invoicerJwtVerifier.verify(refreshToken) == null) {
            unauthorizedError()
        }

        return AuthTokenModel(
            accessToken = tokenManager.generateToken(user.id.toString()),
            refreshToken = tokenManager.generateRefreshToken(user.id.toString())
        )
    }

    private fun peekUserId(refreshToken: String): String {
        return invoicerJwtVerifier.verify(refreshToken) ?: unauthorizedError()
    }
}