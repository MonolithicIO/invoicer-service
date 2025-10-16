package io.github.monolithic.invoicer.models.resetpassword

import java.util.*
import kotlinx.datetime.Instant

data class CreateResetPasswordRequestModel(
    val safeCode: String,
    val userId: UUID,
    val expiresAt: Instant
)
