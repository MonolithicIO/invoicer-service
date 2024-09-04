package domains.user.data.api.repository

import domains.user.data.api.datasource.UserDataSource
import domains.user.data.api.extensions.toModel
import domains.user.domain.models.CreateUserModel
import domains.user.domain.models.UserModel
import domains.user.domain.repository.UserRepository
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

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
