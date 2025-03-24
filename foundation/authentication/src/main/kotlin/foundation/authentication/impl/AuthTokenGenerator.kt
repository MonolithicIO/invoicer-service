package foundation.authentication.impl

internal interface AuthTokenGenerator {
    fun generateAccessToken(userId: String): String
    fun generateRefreshToken(userId: String): String
}