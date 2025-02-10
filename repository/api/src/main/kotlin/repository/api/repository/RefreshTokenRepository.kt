package repository.api.repository

import datasource.api.database.RefreshTokenDatabaseSource
import models.login.RefreshTokenModel

interface RefreshTokenRepository {

    suspend fun createRefreshToken(
        token: String,
        userId: String
    )

    suspend fun invalidateToken(
        userId: String,
        token: String
    )

    suspend fun invalidateAllUserTokens(
        userId: String
    )

    suspend fun findUserToken(
        userId: String,
        token: String
    ): RefreshTokenModel?
}

internal class RefreshTokenRepositoryImpl(
    private val databaseSource: RefreshTokenDatabaseSource
) : RefreshTokenRepository {

    override suspend fun createRefreshToken(token: String, userId: String) {
        return databaseSource.createRefreshToken(
            token = token,
            userId = userId
        )
    }

    override suspend fun invalidateToken(
        userId: String,
        token: String,
    ) {
        return databaseSource.invalidateToken(
            userId = userId,
            token = token
        )
    }

    override suspend fun invalidateAllUserTokens(userId: String) {
        return databaseSource.invalidateAllUserTokens(
            userId = userId
        )
    }

    override suspend fun findUserToken(userId: String, token: String): RefreshTokenModel? {
        val response = databaseSource.findUserToken(
            userId = userId,
            token = token
        )

        return response
    }
}