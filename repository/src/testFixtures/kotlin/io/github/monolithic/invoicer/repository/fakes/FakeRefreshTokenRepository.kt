package io.github.monolithic.invoicer.repository.fakes

import io.github.monolithic.invoicer.models.login.RefreshTokenModel
import io.github.monolithic.invoicer.repository.RefreshTokenRepository
import java.util.*
import kotlinx.datetime.Instant

class FakeRefreshTokenRepository : RefreshTokenRepository {

    var invalidateCallStack = mutableListOf<Pair<UUID, String>>()

    var userToken: suspend () -> RefreshTokenModel? = { null }

    var createCalls = 0
        private set

    override suspend fun createRefreshToken(
        token: String,
        userId: UUID,
        expiration: Instant
    ) {
        createCalls++
    }

    override suspend fun invalidateToken(userId: UUID, token: String) {
        invalidateCallStack.add(userId to token)
    }

    override suspend fun invalidateAllUserTokens(userId: UUID) = Unit

    override suspend fun findUserToken(token: String): RefreshTokenModel? {
        return userToken()
    }
}