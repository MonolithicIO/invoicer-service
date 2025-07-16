package io.github.monolithic.invoicer.repository.datasource

import io.github.monolithic.invoicer.repository.entities.UserEntity
import io.github.monolithic.invoicer.repository.entities.UserTable
import io.github.monolithic.invoicer.repository.mapper.toModel
import kotlinx.datetime.Clock
import io.github.monolithic.invoicer.models.user.CreateUserModel
import io.github.monolithic.invoicer.models.user.UserModel
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

interface UserDataSource {
    suspend fun getUserByEmail(email: String): UserModel?
    suspend fun getUserById(id: UUID): UserModel?
    suspend fun createUser(data: CreateUserModel): String
    suspend fun deleteUser(id: UUID)
}

internal class UserDataSourceImpl(
    private val clock: Clock
) : UserDataSource {

    override suspend fun getUserByEmail(email: String): UserModel? {
        return newSuspendedTransaction {
            UserEntity.Companion.find { UserTable.email eq email }.firstOrNull()?.toModel()
        }
    }

    override suspend fun getUserById(id: UUID): UserModel? {
        return newSuspendedTransaction {
            UserEntity.Companion.findById(id)?.toModel()
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
            UserTable.deleteWhere { op ->
                UserTable.id.eq(id)
            }
        }
    }
}
