package io.github.monolithic.invoicer.repository.datasource

import io.github.monolithic.invoicer.models.user.CreateUserModel
import io.github.monolithic.invoicer.models.user.UserModel
import io.github.monolithic.invoicer.repository.entities.UserEntity
import io.github.monolithic.invoicer.repository.entities.UserTable
import io.github.monolithic.invoicer.repository.mapper.toModel
import java.util.*
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update

interface UserDataSource {
    suspend fun getUserByEmail(email: String): UserModel?
    suspend fun getUserById(id: UUID): UserModel?
    suspend fun createUser(data: CreateUserModel): String
    suspend fun deleteUser(id: UUID)
    suspend fun updateUserPassword(id: UUID, newPassword: String)
}

internal class UserDataSourceImpl(
    private val clock: Clock
) : UserDataSource {

    override suspend fun getUserByEmail(email: String): UserModel? {
        return newSuspendedTransaction {
            UserEntity.find { UserTable.email eq email }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getUserById(id: UUID): UserModel? {
        return newSuspendedTransaction {
            UserEntity.findById(id)?.toModel()
        }
    }

    override suspend fun createUser(data: CreateUserModel): String {
        return newSuspendedTransaction {
            UserTable.insertAndGetId {
                it[verified] = true
                it[email] = data.email
                it[password] = data.password
                it[updatedAt] = clock.now()
                it[createdAt] = clock.now()
                it[identityProviderUuid] = data.identityProviderUuid
            }.value.toString()
        }
    }

    override suspend fun deleteUser(id: UUID) {
        return newSuspendedTransaction {
            UserTable.deleteWhere { _ ->
                UserTable.id.eq(id)
            }
        }
    }

    override suspend fun updateUserPassword(id: UUID, newPassword: String) {
        return newSuspendedTransaction {
            UserTable.update(
                where = { UserTable.id.eq(id) }
            ) { userEntry ->
                userEntry[password] = newPassword
                userEntry[updatedAt] = clock.now()
            }
        }
    }
}
