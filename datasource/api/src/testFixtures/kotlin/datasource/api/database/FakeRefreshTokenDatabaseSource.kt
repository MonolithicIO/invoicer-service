package datasource.api.database

import models.login.RefreshTokenModel
import models.fixtures.refreshTokenModelFixture

class FakeRefreshTokenDatabaseSource : RefreshTokenDatabaseSource {

    var createRefreshTokenResponse: suspend () -> Unit = {}
    var invalidateTokenResponse: suspend () -> Unit = {}
    var invalidateAllUserTokensResponse: suspend () -> Unit = {}
    var findUserTokenResponse: suspend () -> RefreshTokenModel? = { refreshTokenModelFixture }

    override suspend fun createRefreshToken(token: String, userId: String) {
        return createRefreshTokenResponse()
    }

    override suspend fun invalidateToken(userId: String, token: String) {
        return invalidateTokenResponse()
    }

    override suspend fun invalidateAllUserTokens(userId: String) {
        return invalidateAllUserTokensResponse()
    }

    override suspend fun findUserToken(userId: String, token: String): RefreshTokenModel? {
        return findUserTokenResponse()
    }
}