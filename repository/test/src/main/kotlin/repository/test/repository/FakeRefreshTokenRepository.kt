package repository.test.repository

import models.login.RefreshTokenModel
import repository.api.repository.RefreshTokenRepository

class FakeRefreshTokenRepository : RefreshTokenRepository {

    var invalidateCallStack = mutableListOf<Pair<String, String>>()

    var userToken: suspend () -> RefreshTokenModel? = { null }

    var storeCalls = 0
        private set

    override suspend fun createRefreshToken(token: String, userId: String) {
        storeCalls++
    }

    override suspend fun invalidateToken(userId: String, token: String) {
        invalidateCallStack.add(userId to token)
    }

    override suspend fun invalidateAllUserTokens(userId: String) = Unit

    override suspend fun findUserToken(userId: String, token: String): RefreshTokenModel? {
        return userToken()
    }
}