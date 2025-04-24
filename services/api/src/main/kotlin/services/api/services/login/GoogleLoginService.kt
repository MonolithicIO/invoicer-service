package services.api.services.login

import models.login.AuthTokenModel

interface GoogleLoginService {
    suspend fun login(token: String): AuthTokenModel
}