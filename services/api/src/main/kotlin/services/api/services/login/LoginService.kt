package services.api.services.login

import models.login.LoginModel

interface LoginService {
    suspend fun login(model: LoginModel): LoginPayload
}

data class LoginPayload(
    val accessToken: String,
    val refreshToken: String
)