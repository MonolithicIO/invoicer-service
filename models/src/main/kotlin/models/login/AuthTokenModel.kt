package models.login

data class AuthTokenModel(
    val accessToken: String,
    val refreshToken: String
)
