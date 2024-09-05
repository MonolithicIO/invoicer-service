package io.github.alaksion.invoicer.server.data.datasource

import entities.UserEntity
import entities.UserTable
import io.github.alaksion.invoicer.server.domain.model.user.CreateUserModel
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import java.util.*

internal interface UserDataSource {
    suspend fun getUserByEmail(email: String): UserEntity?
    suspend fun getUserById(id: UUID): UserEntity?
    suspend fun createUser(data: CreateUserModel): String
    suspend fun deleteUser(id: UUID)
}

internal class UserDataSourceImpl : UserDataSource {

    override suspend fun getUserByEmail(email: String): UserEntity? {
        return UserEntity.find { UserTable.email eq email }.firstOrNull()
    }

    override suspend fun getUserById(id: UUID): UserEntity? {
        return UserEntity.findById(id)
    }

    override suspend fun createUser(data: CreateUserModel): String {
        return UserEntity.new {
            verified = true
            email = data.email
            password = data.password
        }.id.value.toString()
    }

    override suspend fun deleteUser(id: UUID) {
        UserTable.deleteWhere { op ->
            UserTable.id.eq(id)
        }
    }

}