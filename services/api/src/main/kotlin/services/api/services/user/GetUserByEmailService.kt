package services.api.services.user

import models.user.UserModel

interface GetUserByEmailService {
    suspend fun get(email: String): UserModel?
}
