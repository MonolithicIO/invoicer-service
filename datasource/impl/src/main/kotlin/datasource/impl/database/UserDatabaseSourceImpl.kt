package datasource.impl.database

import datasource.api.database.UserDatabaseSource
import datasource.api.model.user.CreateUserData
import datasource.impl.mapper.toModel
import entities.UserEntity
import entities.UserTable
import models.user.UserModel
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import utils.date.impl.DateProvider
import java.util.*

internal class UserDatabaseSourceImpl(
    private val dateProvider: DateProvider
) : UserDatabaseSource {

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

    override suspend fun createUser(data: CreateUserData): String {
        return newSuspendedTransaction {
            UserTable.insertAndGetId {
                it[verified] = true
                it[email] = data.email
                it[password] = data.password
                it[updatedAt] = dateProvider.currentInstant()
                it[createdAt] = dateProvider.currentInstant()
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