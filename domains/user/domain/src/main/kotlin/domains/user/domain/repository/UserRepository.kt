package domains.user.domain.repository


import domains.user.domain.models.CreateUserModel
import domains.user.domain.models.UserModel
import java.util.UUID

interface UserRepository {
    suspend fun getUserByEmail(email: String): UserModel?
    suspend fun getUserById(id: UUID): UserModel?
    suspend fun createUser(data: CreateUserModel): String
    suspend fun deleteUser(id: UUID)
}