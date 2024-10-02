package io.github.alaksion.invoicer.server.view.viewmodel.createuser

import kotlinx.serialization.Serializable
import models.user.CreateUserModel
import utils.exceptions.badRequestError

@Serializable
data class CreateUserRequestViewModel(
    val email: String? = null,
    val confirmEmail: String? = null,
    val password: String? = null,
)

internal fun CreateUserRequestViewModel.toDomainModel(): CreateUserModel {
    if (this.email == null) badRequestError(message = "E-mail field is required")
    if (this.password == null) badRequestError(message = "password field is required")
    if (this.confirmEmail == null) badRequestError(message = "confirm e-mail field is required")

    return CreateUserModel(
        email = this.email.trim(),
        password = this.password.trim(),
        confirmEmail = this.confirmEmail.trim()
    )
}

@Serializable
data class CreateUserResponseViewModel(
    val userId: String
)


