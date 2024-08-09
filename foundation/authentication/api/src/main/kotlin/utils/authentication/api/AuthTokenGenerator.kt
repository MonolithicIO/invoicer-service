package utils.authentication.api

internal interface AuthTokenGenerator {
    fun generateToken(userId: String): String

    enum class Tags {
        Jwt;
    }
}