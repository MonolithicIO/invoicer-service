package io.github.monolithic.invoicer.controller.viewmodel.user

import io.github.monolithic.invoicer.controller.validation.requiredString
import kotlinx.serialization.Serializable

@Serializable
internal data class RequestPasswordResetViewModel(
    val email: String? = null
)

@Serializable
internal data class RequestPasswordResetResponseViewModel(
    val resetToken: String
)

@Serializable
internal data class VerifyPasswordResetViewModel(
    val pinCode: String? = null,
) {
    fun toParam(): String {
        return requiredString(value = pinCode, missingErrorMessage = "Pin code is required.")
    }
}

@Serializable
internal data class ResetPasswordViewModel(
    val newPassword: String? = null,
    val confirmPassword: String? = null
)
