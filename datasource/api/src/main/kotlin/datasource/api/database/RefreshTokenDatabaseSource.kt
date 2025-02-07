package datasource.api.database

import entities.RefreshTokenEntity

interface RefreshTokenDatabaseSource {

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
    ): RefreshTokenEntity?

}