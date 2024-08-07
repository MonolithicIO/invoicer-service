package io.github.alaksion.invoicer.server.view.viewmodel.createuser

import io.github.alaksion.invoicer.server.domain.errors.badRequestError
import io.github.alaksion.invoicer.server.domain.model.user.CreateUserModel
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequestViewModel(
    val email: String? = null,
    val confirmEmail: String? = null,
    val password: String? = null,
)

internal fun CreateUserRequestViewModel.toDomainModel(): CreateUserModel {
    if (this.email.isNullOrBlank()) badRequestError(message = "E-mail field is required")
    if (this.password.isNullOrBlank()) badRequestError(message = "password field is required")
    if (this.confirmEmail.isNullOrBlank()) badRequestError(message = "confirm e-mail field is required")

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
