package io.github.monolithic.invoicer.repository

import io.github.monolithic.invoicer.models.login.RefreshTokenModel
import io.github.monolithic.invoicer.repository.datasource.RefreshTokenDataSource
import java.util.*

interface RefreshTokenRepository {

    suspend fun createRefreshToken(
        token: String,
        userId: UUID
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

    override suspend fun createRefreshToken(token: String, userId: UUID) {
        return dataSource.createRefreshToken(
            token = token,
            userId = userId
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
