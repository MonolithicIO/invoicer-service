package io.github.alaksion.invoicer.server.data.datasource

import entities.UserEntity
import entities.UserTable
import io.github.alaksion.invoicer.server.domain.model.user.CreateUserModel
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import utils.date.api.DateProvider
import java.util.*

internal interface UserDataSource {
    suspend fun getUserByEmail(email: String): UserEntity?
    suspend fun getUserById(id: UUID): UserEntity?
    suspend fun createUser(data: CreateUserModel): String
    suspend fun deleteUser(id: UUID)
}

internal class UserDataSourceImpl(
    private val dateProvider: DateProvider
) : UserDataSource {

    override suspend fun getUserByEmail(email: String): UserEntity? {
        return UserEntity.find { UserTable.email eq email }.firstOrNull()
    }

    override suspend fun getUserById(id: UUID): UserEntity? {
        return UserEntity.findById(id)
    }

    override suspend fun createUser(data: CreateUserModel): String {
        return UserTable.insertAndGetId {
            it[verified] = true
            it[email] = data.email
            it[password] = data.password
            it[updatedAt] = dateProvider.now()
            it[createdAt] = dateProvider.now()
        }.value.toString()
    }

    override suspend fun deleteUser(id: UUID) {
        UserTable.deleteWhere { op ->
            UserTable.id.eq(id)
        }
    }

}