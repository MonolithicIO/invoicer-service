package utils.authentication.api

interface AuthTokenManager {
    fun generateToken(userId: String): String

    companion object {
        val NOT_AUTHENTICATED_MESSAGE = "Session is invalid or expired"
    }
}

internal class AuthTokenManagerImpl(
    private val tokenGenerator: AuthTokenGenerator
) : AuthTokenManager {

    override fun generateToken(userId: String): String {
        return tokenGenerator.generateToken(userId)
    }

}