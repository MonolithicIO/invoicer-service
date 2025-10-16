package io.github.monolithic.invoicer.controller.viewmodel.user

import kotlinx.serialization.Serializable

@Serializable
internal data class RequestPasswordResetViewModel(
    val email: String
)

@Serializable
internal data class RequestPasswordResetResponseViewModel(
    val resetToken: String
)
