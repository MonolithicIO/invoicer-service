package services.api.services.login

import models.login.AuthTokenModel
import models.login.LoginModel

interface LoginService {
    suspend fun login(model: LoginModel): AuthTokenModel
}