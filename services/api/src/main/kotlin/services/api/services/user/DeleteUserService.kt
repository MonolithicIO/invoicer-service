package services.api.services.user

interface DeleteUserService {
    suspend fun delete(userId: String)
}