package services.api.services.login

import models.login.LoginModel

interface LoginService {
    suspend fun login(model: LoginModel): String
}