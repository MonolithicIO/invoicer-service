package io.github.monolithic.invoicer.repository

import io.github.monolithic.invoicer.models.user.CreateUserModel
import io.github.monolithic.invoicer.models.user.UserModel
import io.github.monolithic.invoicer.repository.datasource.UserDataSource
import java.util.*

interface UserRepository {
    suspend fun getUserByEmail(email: String): UserModel?
    suspend fun getUserById(id: UUID): UserModel?
    suspend fun createUser(data: CreateUserModel): String
    suspend fun deleteUser(id: UUID)
    suspend fun updatePassword(id: UUID, newPassword: String)
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

    override suspend fun updatePassword(id: UUID, newPassword: String) {
        return userDataSource.updateUserPassword(id = id, newPassword = newPassword)
    }
}
