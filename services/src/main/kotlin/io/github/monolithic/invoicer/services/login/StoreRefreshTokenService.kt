package io.github.monolithic.invoicer.services.login

import java.util.UUID
import io.github.monolithic.invoicer.repository.RefreshTokenRepository
import kotlin.time.Duration.Companion.days
import kotlinx.datetime.Clock

interface StoreRefreshTokenService {
    suspend fun createRefreshToken(
        token: String,
        userId: UUID,
    )
}

internal class StoreRefreshTokenServiceImpl(
    private val refreshTokenRepository: RefreshTokenRepository,
    private val clock: Clock
) : StoreRefreshTokenService {

    override suspend fun createRefreshToken(token: String, userId: UUID) {
        refreshTokenRepository.createRefreshToken(
            token = token,
            userId = userId,
            expiration = clock.now().plus(REFRESH_TOKEN_EXPIRATION_DAYS.days)
        )
    }

    companion object {
        const val REFRESH_TOKEN_EXPIRATION_DAYS = 90
    }
}
