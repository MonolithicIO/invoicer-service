package io.github.monolithic.invoicer.models.resetpassword

import java.util.*
import kotlinx.datetime.Instant

data class ResetPasswordRequestModel(
    val id: UUID,
    val safeCode: String,
    val userId: UUID,
    val isConsumed: Boolean,
    val expirationText: String,
    val expiresAt: Instant,
    val createdAt: Instant,
    val updatedAt: Instant,
    val attempts: Int
)
