package io.github.monolithic.invoicer.models.login

import kotlinx.datetime.Instant
import java.util.UUID

data class RefreshTokenModel(
    val userId: UUID,
    val token: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val enabled: Boolean,
    val expiresAt: Instant
)
