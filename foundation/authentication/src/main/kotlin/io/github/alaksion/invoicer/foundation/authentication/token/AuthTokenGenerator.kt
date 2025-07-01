package io.github.alaksion.invoicer.foundation.authentication.token

internal interface AuthTokenGenerator {
    fun generateAccessToken(userId: String): String
    fun generateRefreshToken(userId: String): String
}