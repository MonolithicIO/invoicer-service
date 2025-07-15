package io.github.monolithic.invoicer.foundation.authentication.token

internal interface AuthTokenGenerator {
    fun generateAccessToken(userId: String): String
    fun generateRefreshToken(userId: String): String
}
