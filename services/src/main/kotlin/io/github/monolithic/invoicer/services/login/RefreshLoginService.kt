package io.github.monolithic.invoicer.services.login

import io.github.monolithic.invoicer.foundation.authentication.token.AuthTokenManager
import io.github.monolithic.invoicer.foundation.exceptions.http.unAuthorizedError
import io.github.monolithic.invoicer.models.login.AuthTokenModel
import io.github.monolithic.invoicer.repository.RefreshTokenRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import kotlinx.datetime.Clock

interface RefreshLoginService {
    suspend fun refreshLogin(
        refreshToken: String,
    ): AuthTokenModel
}

internal class RefreshLoginServiceImpl(
    private val tokenManager: AuthTokenManager,
    private val getUserByIdService: GetUserByIdService,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val storeRefreshTokenService: StoreRefreshTokenService,
    private val clock: Clock
) : RefreshLoginService {

    override suspend fun refreshLogin(refreshToken: String): AuthTokenModel {
        val findToken =
            refreshTokenRepository.findUserToken(refreshToken) ?: unAuthorizedError(
                message = "Refresh token already consumed"
            )

        if (clock.now() > findToken.expiresAt) {
            unAuthorizedError("Refresh token expired")
        }

        if (findToken.enabled.not()) {
            unAuthorizedError("Invalid refresh token")
        }

        val user = getUserByIdService.get(findToken.userId)
        val newAuthToken = tokenManager.generateToken(user.id.toString())
        val newRefreshToken = tokenManager.generateRefreshToken()

        refreshTokenRepository.invalidateToken(
            userId = user.id,
            token = refreshToken
        )

        storeRefreshTokenService.createRefreshToken(
            token = newRefreshToken,
            userId = user.id
        )

        return AuthTokenModel(
            refreshToken = newRefreshToken,
            accessToken = newAuthToken
        )
    }
}
