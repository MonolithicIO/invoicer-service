package datasource.impl.database

import datasource.api.database.UserDatabaseSource
import datasource.api.model.user.CreateUserData
import entities.UserEntity
import entities.UserTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import utils.date.impl.DateProvider
import java.util.*

internal class UserDatabaseSourceImpl(
    private val dateProvider: DateProvider
) : UserDatabaseSource {

    override suspend fun getUserByEmail(email: String): UserEntity? {
        return newSuspendedTransaction {
            UserEntity.find { UserTable.email eq email }.firstOrNull()
        }
    }

    override suspend fun getUserById(id: UUID): UserEntity? {
        return newSuspendedTransaction {
            UserEntity.findById(id)
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