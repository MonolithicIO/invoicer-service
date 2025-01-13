package models.login

import kotlinx.datetime.Instant

data class RefreshTokenModel(
    val userId: String,
    val token: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val enabled: Boolean
)
