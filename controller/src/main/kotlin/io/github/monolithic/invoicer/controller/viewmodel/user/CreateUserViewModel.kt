package io.github.monolithic.invoicer.controller.viewmodel.user

import kotlinx.serialization.Serializable
import io.github.monolithic.invoicer.models.user.CreateUserModel
import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError

@Serializable
internal data class CreateUserRequestViewModel(
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
internal data class CreateUserResponseViewModel(
    val userId: String
)


