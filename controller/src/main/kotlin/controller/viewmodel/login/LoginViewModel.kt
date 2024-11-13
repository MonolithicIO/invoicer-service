package controller.viewmodel.login

import kotlinx.serialization.Serializable
import models.login.LoginModel
import services.api.services.login.LoginPayload
import utils.exceptions.badRequestError

@Serializable
internal data class LoginViewModel(
    val email: String? = null,
    val password: String? = null
)

internal fun LoginViewModel.toDomainModel(): LoginModel =
    LoginModel(
        email = this.email ?: badRequestError("Missing e-mail field"),
        password = this.password ?: badRequestError("Missing password field")
    )

@Serializable
internal data class LoginResponseViewModel(
    val token: String,
    val refreshToken: String
)

internal fun LoginPayload.toViewModel(): LoginResponseViewModel =
    LoginResponseViewModel(
        token = this.accessToken,
        refreshToken = this.refreshToken
    )
