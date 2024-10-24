package services.api.services.user

import models.user.UserModel

interface GetUserByIdService {
    suspend fun get(id: String): UserModel
}