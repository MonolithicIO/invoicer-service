package io.github.monolithic.invoicer.controller.viewmodel.login

import kotlinx.serialization.Serializable
import io.github.monolithic.invoicer.models.login.AuthTokenModel
import io.github.monolithic.invoicer.models.login.LoginModel
import io.github.monolithic.invoicer.foundation.exceptions.http.badRequestError

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

internal fun AuthTokenModel.toViewModel(): LoginResponseViewModel =
    LoginResponseViewModel(
        token = this.accessToken,
        refreshToken = this.refreshToken
    )
