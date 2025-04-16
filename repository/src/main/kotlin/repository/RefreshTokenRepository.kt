package repository

import datasource.api.database.RefreshTokenDatabaseSource
import models.login.RefreshTokenModel
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
        userId: UUID,
        token: String
    ): RefreshTokenModel?
}

internal class RefreshTokenRepositoryImpl(
    private val databaseSource: RefreshTokenDatabaseSource
) : RefreshTokenRepository {

    override suspend fun createRefreshToken(token: String, userId: UUID) {
        return databaseSource.createRefreshToken(
            token = token,
            userId = userId
        )
    }

    override suspend fun invalidateToken(
        userId: UUID,
        token: String,
    ) {
        return databaseSource.invalidateToken(
            userId = userId,
            token = token
        )
    }

    override suspend fun invalidateAllUserTokens(userId: UUID) {
        return databaseSource.invalidateAllUserTokens(
            userId = userId
        )
    }

    override suspend fun findUserToken(userId: UUID, token: String): RefreshTokenModel? {
        val response = databaseSource.findUserToken(
            userId = userId,
            token = token
        )

        return response
    }
}