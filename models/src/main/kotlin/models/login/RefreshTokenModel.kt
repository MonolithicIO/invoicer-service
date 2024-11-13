package models.login

import kotlinx.datetime.LocalDate

data class RefreshTokenModel(
    val userId: String,
    val token: String,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
    val enabled: Boolean
)
