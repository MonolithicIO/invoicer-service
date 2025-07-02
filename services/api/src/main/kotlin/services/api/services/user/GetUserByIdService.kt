package services.api.services.user

import models.user.UserModel
import java.util.*

interface GetUserByIdService {
    suspend fun get(id: UUID): UserModel
}
