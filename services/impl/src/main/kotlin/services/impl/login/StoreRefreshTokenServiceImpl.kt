package services.impl.login

import repository.api.repository.RefreshTokenRepository
import services.api.services.login.StoreRefreshTokenService
import java.util.*

internal class StoreRefreshTokenServiceImpl(
    private val refreshTokenRepository: RefreshTokenRepository
) : StoreRefreshTokenService {

    override suspend fun storeRefreshToken(token: String, userId: UUID) {
        refreshTokenRepository.createRefreshToken(
            token = token,
            userId = userId.toString()
        )
    }
}