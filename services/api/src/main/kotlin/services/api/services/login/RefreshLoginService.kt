package services.api.services.login

import models.login.AuthTokenModel

interface RefreshLoginService {
    suspend fun refreshLogin(
        refreshToken: String,
    ): AuthTokenModel
}
