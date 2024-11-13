package foundation.authentication.api

class FakeAuthTokenManager : AuthTokenManager {

    var token = "1234"
    var refreshToken = "9876"
    var verify = "user-id"

    override fun generateToken(userId: String): String {
        return token
    }

    override fun generateRefreshToken(userId: String): String {
        return refreshToken
    }

    override fun verifyToken(token: String): String? {
        return verify
    }
}