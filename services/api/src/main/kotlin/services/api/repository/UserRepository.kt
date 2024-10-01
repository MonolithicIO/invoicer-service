package services.api.repository

import services.api.model.user.CreateUserModel
import services.api.model.user.UserModel
import java.util.UUID

interface UserRepository {
    suspend fun getUserByEmail(email: String): UserModel?
    suspend fun getUserById(id: UUID): UserModel?
    suspend fun createUser(data: CreateUserModel): String
    suspend fun deleteUser(id: UUID)
}
