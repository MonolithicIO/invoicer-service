package io.github.alaksion.invoicer.server.view.viewmodel.createuser.request

import io.github.alaksion.invoicer.server.domain.errors.badRequestError
import io.github.alaksion.invoicer.server.domain.model.user.CreateUserModel
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequestViewModel(
    val email: String? = null,
    val confirmEmail: String? = null,
    val password: String? = null,
)

fun receiveUserRequest(request: CreateUserRequestViewModel): CreateUserModel {
    if (request.email.isNullOrBlank()) badRequestError(message = "E-mail field is required")
    if (request.password.isNullOrBlank()) badRequestError(message = "password field is required")
    if (request.confirmEmail.isNullOrBlank()) badRequestError(message = "confirm e-mail field is required")

    return CreateUserModel(
        email = request.email.trim(),
        password = request.password.trim(),
        confirmEmail = request.confirmEmail.trim()
    )
}
