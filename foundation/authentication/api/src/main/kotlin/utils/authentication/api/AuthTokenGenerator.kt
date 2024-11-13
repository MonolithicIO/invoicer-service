package utils.authentication.api

internal interface AuthTokenGenerator {
    fun generateAccessToken(userId: String): String
    fun generateRefreshToken(userId: String): String

    enum class Tags {
        Jwt;
    }
}