package io.github.monolithic.invoicer.services.login

import io.github.monolithic.invoicer.foundation.authentication.token.AuthTokenManager
import io.github.monolithic.invoicer.foundation.exceptions.http.forbiddenError
import io.github.monolithic.invoicer.foundation.exceptions.http.unAuthorizedError
import io.github.monolithic.invoicer.models.login.AuthTokenModel
import io.github.monolithic.invoicer.repository.RefreshTokenRepository
import io.github.monolithic.invoicer.services.user.GetUserByIdService
import io.github.monolithic.invoicer.utils.uuid.parseUuid

interface RefreshLoginService {
    suspend fun refreshLogin(
        refreshToken: String,
    ): AuthTokenModel
}

internal class RefreshLoginServiceImpl(
    private val tokenManager: AuthTokenManager,
    private val getUserByIdService: GetUserByIdService,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val storeRefreshTokenService: StoreRefreshTokenService
) : RefreshLoginService {

    override suspend fun refreshLogin(refreshToken: String): AuthTokenModel {
        val extractedUserId = tokenManager.verifyToken(refreshToken) ?: unAuthorizedError(
            message = "Invalid refresh token"
        )

        val user = getUserByIdService.get(parseUuid(extractedUserId))

        val findToken =
            refreshTokenRepository.findUserToken(refreshToken) ?: unAuthorizedError(
                message = "Refresh token already consumed"
            )

        if (findToken.enabled.not()) {
            forbiddenError()
        }

        val authResponse = AuthTokenModel(
            accessToken = tokenManager.generateToken(user.id.toString()),
            refreshToken = tokenManager.generateRefreshToken(user.id.toString())
        )


        refreshTokenRepository.invalidateToken(
            userId = user.id,
            token = refreshToken
        )

        storeRefreshTokenService.storeRefreshToken(
            token = authResponse.refreshToken,
            userId = user.id
        )

        return authResponse
    }
}
