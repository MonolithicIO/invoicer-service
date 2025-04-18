package models.qrcodetoken

import kotlinx.serialization.Serializable

@Serializable
data class AuthorizedQrCodeToken(
    val rawContent: String,
    val refreshToken: String,
    val accessToken: String
)
