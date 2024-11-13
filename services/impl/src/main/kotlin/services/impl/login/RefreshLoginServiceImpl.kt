package services.impl.login

import models.login.AuthTokenModel
import repository.api.repository.RefreshTokenRepository
import services.api.services.login.RefreshLoginService
import services.api.services.login.StoreRefreshTokenService
import services.api.services.user.GetUserByIdService
import utils.authentication.api.AuthTokenManager
import utils.authentication.api.jwt.InvoicerJwtVerifier
import utils.exceptions.unauthorizedError

internal class RefreshLoginServiceImpl(
    private val invoicerJwtVerifier: InvoicerJwtVerifier,
    private val tokenManager: AuthTokenManager,
    private val getUserByIdService: GetUserByIdService,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val storeRefreshTokenService: StoreRefreshTokenService
) : RefreshLoginService {

    override suspend fun refreshLogin(refreshToken: String): AuthTokenModel {
        val user = getUserByIdService.get(peekUserId(refreshToken))

        val findToken = refreshTokenRepository.findUserToken(user.id.toString(), refreshToken) ?: unauthorizedError(
            message = "Refresh token not found"
        )

        if (findToken.enabled.not()) {
            unauthorizedError(message = "Refresh token is disabled")
        }

        if (invoicerJwtVerifier.verify(refreshToken) == null) {
            unauthorizedError()
        }

        val authResponse = AuthTokenModel(
            accessToken = tokenManager.generateToken(user.id.toString()),
            refreshToken = tokenManager.generateRefreshToken(user.id.toString())
        )


        refreshTokenRepository.invalidateToken(
            userId = user.id.toString(),
            token = refreshToken
        )

        storeRefreshTokenService.storeRefreshToken(
            token = authResponse.refreshToken,
            userId = user.id.toString()
        )

        return authResponse
    }

    private fun peekUserId(refreshToken: String): String {
        return invoicerJwtVerifier.verify(refreshToken) ?: unauthorizedError()
    }
}