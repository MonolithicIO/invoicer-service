package services.api.services.user

import java.util.UUID

interface DeleteUserService {
    suspend fun delete(userId: UUID)
}