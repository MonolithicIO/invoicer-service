package io.github.monolithic.invoicer.models.resetpassword

import java.util.*
import kotlinx.datetime.Instant

data class ResetPasswordRequestModel(
    val safeCode: String,
    val userId: UUID,
    val isConsumed: String,
    val expiresAt: Instant,
    val createdAt: Instant,
    val updatedAt: Instant,
)
