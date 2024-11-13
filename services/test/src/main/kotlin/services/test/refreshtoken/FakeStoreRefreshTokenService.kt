package services.test.refreshtoken

import services.api.services.login.StoreRefreshTokenService

class FakeStoreRefreshTokenService : StoreRefreshTokenService {

    var response: suspend () -> Unit = suspend { }

    var callHistory = mutableListOf<Pair<String, String>>()

    override suspend fun storeRefreshToken(token: String, userId: String) {
        callHistory.add(Pair(token, userId))
        response()
    }
}