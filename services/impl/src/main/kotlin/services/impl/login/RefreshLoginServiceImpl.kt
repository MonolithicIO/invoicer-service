package services.impl.login

import foundation.authentication.impl.AuthTokenManager
import io.github.alaksion.invoicer.utils.uuid.parseUuid
import models.login.AuthTokenModel
import repository.RefreshTokenRepository
import services.api.services.login.RefreshLoginService
import services.api.services.login.StoreRefreshTokenService
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.unAuthorizedError
import utils.exceptions.http.forbiddenError

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
            refreshTokenRepository.findUserToken(user.id, refreshToken) ?: unAuthorizedError(
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