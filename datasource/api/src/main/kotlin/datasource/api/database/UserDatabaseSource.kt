package datasource.api.database

import datasource.api.model.user.CreateUserData
import models.user.UserModel
import java.util.*

interface UserDatabaseSource {
    suspend fun getUserByEmail(email: String): UserModel?
    suspend fun getUserById(id: UUID): UserModel?
    suspend fun createUser(data: CreateUserData): String
    suspend fun deleteUser(id: UUID)
}
