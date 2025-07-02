package services.api.services.user

import models.user.CreateUserModel

interface CreateUserService {
    suspend fun create(userModel: CreateUserModel): String
}
