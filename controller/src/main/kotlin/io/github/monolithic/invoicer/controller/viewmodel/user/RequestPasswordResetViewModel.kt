package io.github.monolithic.invoicer.controller.viewmodel.user

import kotlinx.serialization.Serializable

@Serializable
internal data class RequestPasswordResetViewModel(
    val email: String? = null
)

@Serializable
internal data class RequestPasswordResetResponseViewModel(
    val resetToken: String
)
