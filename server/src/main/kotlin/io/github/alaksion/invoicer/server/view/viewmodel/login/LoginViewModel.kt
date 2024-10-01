package io.github.alaksion.invoicer.server.view.viewmodel.login

import kotlinx.serialization.Serializable
import services.api.model.login.LoginModel
import utils.exceptions.badRequestError

@Serializable
data class LoginViewModel(
    val email: String? = null,
    val password: String? = null
)

internal fun LoginViewModel.toDomainModel(): LoginModel =
    LoginModel(
        email = this.email ?: badRequestError("Missing e-mail field"),
        password = this.password ?: badRequestError("Missing password field")
    )

@Serializable
data class LoginResponseViewModel(
    val token: String
)
