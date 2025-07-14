package repository

import java.util.*
import models.user.CreateUserModel
import models.user.UserModel
import repository.datasource.UserDataSource

interface UserRepository {
    suspend fun getUserByEmail(email: String): UserModel?
    suspend fun getUserById(id: UUID): UserModel?
    suspend fun createUser(data: CreateUserModel): String
    suspend fun deleteUser(id: UUID)
}

internal class UserRepositoryImpl(
    private val userDataSource: UserDataSource
) : UserRepository {

    override suspend fun getUserByEmail(email: String): UserModel? {
        return userDataSource.getUserByEmail(email = email)
    }

    override suspend fun getUserById(id: UUID): UserModel? {
        return userDataSource.getUserById(id = id)
    }

    override suspend fun createUser(data: CreateUserModel): String {
        return userDataSource.createUser(data = data)
    }

    override suspend fun deleteUser(id: UUID) {
        return userDataSource.deleteUser(id = id)
    }
}
