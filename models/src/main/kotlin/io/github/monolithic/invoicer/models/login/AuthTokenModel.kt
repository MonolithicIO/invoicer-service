package io.github.monolithic.invoicer.models.login

data class AuthTokenModel(
    val accessToken: String,
    val refreshToken: String
)
