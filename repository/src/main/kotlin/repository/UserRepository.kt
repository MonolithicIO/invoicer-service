package repository

import datasource.api.database.UserDatabaseSource
import datasource.api.model.user.CreateUserData
import models.user.CreateUserModel
import models.user.UserModel
import java.util.*

interface UserRepository {
    suspend fun getUserByEmail(email: String): UserModel?
    suspend fun getUserById(id: UUID): UserModel?
    suspend fun createUser(data: CreateUserModel): String
    suspend fun deleteUser(id: UUID)
}

internal class UserRepositoryImpl(
    private val databaseSource: UserDatabaseSource
) : UserRepository {

    override suspend fun getUserByEmail(email: String): UserModel? {
        return databaseSource.getUserByEmail(email = email)
    }

    override suspend fun getUserById(id: UUID): UserModel? {
        return databaseSource.getUserById(
            id = id
        )
    }

    override suspend fun createUser(data: CreateUserModel): String {
        return databaseSource.createUser(
            data = CreateUserData(
                email = data.email,
                password = data.password,
                identityProviderUuid = data.identityProviderUuid
            )
        )
    }

    override suspend fun deleteUser(id: UUID) {
        return databaseSource.deleteUser(
            id = id
        )
    }
}