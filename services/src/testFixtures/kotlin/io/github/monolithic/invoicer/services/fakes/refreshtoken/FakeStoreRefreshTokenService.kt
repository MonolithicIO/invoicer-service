package io.github.monolithic.invoicer.services.fakes.refreshtoken

import io.github.monolithic.invoicer.services.login.StoreRefreshTokenService
import java.util.*

class FakeStoreRefreshTokenService : StoreRefreshTokenService {

    var response: suspend () -> Unit = suspend { }

    var callHistory = mutableListOf<Pair<String, UUID>>()

    override suspend fun createRefreshToken(token: String, userId: UUID) {
        callHistory.add(Pair(token, userId))
        response()
    }
}