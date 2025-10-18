package io.github.monolithic.invoicer.models.resetpassword

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordToken(
    val token: String,
    val userId: String
)
