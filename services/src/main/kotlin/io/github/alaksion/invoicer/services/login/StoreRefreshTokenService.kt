package io.github.alaksion.invoicer.services.login

import java.util.UUID
import repository.RefreshTokenRepository

interface StoreRefreshTokenService {
    suspend fun storeRefreshToken(token: String, userId: UUID)
}

internal class StoreRefreshTokenServiceImpl(
    private val refreshTokenRepository: RefreshTokenRepository
) : StoreRefreshTokenService {

    override suspend fun storeRefreshToken(token: String, userId: UUID) {
        refreshTokenRepository.createRefreshToken(
            token = token,
            userId = userId
        )
    }
}
