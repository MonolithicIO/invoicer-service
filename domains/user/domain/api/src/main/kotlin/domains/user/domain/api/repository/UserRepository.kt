package domains.user.domain.api.repository


import domains.user.domain.api.models.CreateUserModel
import domains.user.domain.api.models.UserModel
import java.util.UUID

interface UserRepository {
    suspend fun getUserByEmail(email: String): UserModel?
    suspend fun getUserById(id: UUID): UserModel?
    suspend fun createUser(data: CreateUserModel): String
    suspend fun deleteUser(id: UUID)
}