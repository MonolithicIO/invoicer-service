package foundation.authentication.api

class FakeAuthTokenManager : AuthTokenManager {

    var token = "1234"
    var refreshToken = "9876"

    override fun generateToken(userId: String): String {
        return token
    }

    override fun generateRefreshToken(userId: String): String {
        return refreshToken
    }
}