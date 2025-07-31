package io.github.monolithic.invoicer.repository

import io.github.monolithic.invoicer.models.login.RefreshTokenModel
import io.github.monolithic.invoicer.repository.datasource.RefreshTokenDataSource
import java.util.*
import kotlinx.datetime.Instant

interface RefreshTokenRepository {

    suspend fun createRefreshToken(
        token: String,
        userId: UUID,
        expiration: Instant
    )

    suspend fun invalidateToken(
        userId: UUID,
        token: String
    )

    suspend fun invalidateAllUserTokens(
        userId: UUID
    )

    suspend fun findUserToken(
        token: String
    ): RefreshTokenModel?
}

internal class RefreshTokenRepositoryImpl(
    private val dataSource: RefreshTokenDataSource
) : RefreshTokenRepository {

    override suspend fun createRefreshToken(token: String, userId: UUID, expiration: Instant) {
        return dataSource.createRefreshToken(
            token = token,
            userId = userId,
            expiration = expiration
        )
    }

    override suspend fun invalidateToken(
        userId: UUID,
        token: String,
    ) {
        return dataSource.invalidateToken(
            userId = userId,
            token = token
        )
    }

    override suspend fun invalidateAllUserTokens(userId: UUID) {
        return dataSource.invalidateAllUserTokens(userId = userId)
    }

    override suspend fun findUserToken(token: String): RefreshTokenModel? {
        return dataSource.findRefreshToken(
            token = token
        )
    }
}
