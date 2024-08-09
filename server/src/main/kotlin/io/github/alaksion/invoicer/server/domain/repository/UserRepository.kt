package io.github.alaksion.invoicer.server.domain.repository

import io.github.alaksion.invoicer.server.data.datasource.UserDataSource
import io.github.alaksion.invoicer.server.data.entities.toModel
import io.github.alaksion.invoicer.server.domain.model.user.CreateUserModel
import io.github.alaksion.invoicer.server.domain.model.user.UserModel
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

interface UserRepository {
    suspend fun getUserByEmail(email: String): UserModel?
    suspend fun getUserById(id: UUID): UserModel?
    suspend fun createUser(data: CreateUserModel): String
    suspend fun deleteUser(id: UUID)
}

internal class UserRepositoryImpl(
    private val dataSource: UserDataSource
) : UserRepository {

    override suspend fun getUserByEmail(email: String): UserModel? {
        return newSuspendedTransaction {
            dataSource.getUserByEmail(email)?.toModel()
        }
    }

    override suspend fun getUserById(id: UUID): UserModel? {
        return newSuspendedTransaction {
            dataSource.getUserById(id)?.toModel()
        }
    }

    override suspend fun createUser(data: CreateUserModel): String {
        return newSuspendedTransaction {
            dataSource.createUser(data)
        }
    }

    override suspend fun deleteUser(id: UUID) {
        return newSuspendedTransaction {
            dataSource.deleteUser(id)
        }
    }


}