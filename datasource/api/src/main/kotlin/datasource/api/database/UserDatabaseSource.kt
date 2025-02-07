package datasource.api.database

import datasource.api.model.user.CreateUserData
import entities.UserEntity
import java.util.*

interface UserDatabaseSource {
    suspend fun getUserByEmail(email: String): UserEntity?
    suspend fun getUserById(id: UUID): UserEntity?
    suspend fun createUser(data: CreateUserData): String
    suspend fun deleteUser(id: UUID)
}
