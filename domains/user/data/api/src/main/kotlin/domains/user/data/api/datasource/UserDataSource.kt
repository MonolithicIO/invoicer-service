package domains.user.data.api.datasource

import domains.user.domain.models.CreateUserModel
import entities.UserEntity
import entities.UserTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import java.util.UUID

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