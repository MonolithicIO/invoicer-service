package services.impl.login

import repository.api.repository.RefreshTokenRepository
import services.api.services.login.StoreRefreshTokenService

internal class StoreRefreshTokenServiceImpl(
    private val refreshTokenRepository: RefreshTokenRepository
) : StoreRefreshTokenService {

    override suspend fun storeRefreshToken(token: String, userId: String) {
        refreshTokenRepository.createRefreshToken(
            token = token,
            userId = userId
        )
    }
}