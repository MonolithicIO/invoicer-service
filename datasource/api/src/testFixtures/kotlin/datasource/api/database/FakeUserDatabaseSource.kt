package datasource.api.database

import datasource.api.model.user.CreateUserData
import models.user.UserModel
import models.fixtures.userModelFixture
import java.util.*

class FakeUserDatabaseSource : UserDatabaseSource {

    var getUserByEmailResponse: suspend () -> UserModel? = { userModelFixture }
    var getUserByIdResponse: suspend () -> UserModel? = { userModelFixture }
    var createUserResponse: suspend () -> String = { createUserResponseDefault }
    var deleteUserResponse: suspend () -> Unit = {}

    override suspend fun getUserByEmail(email: String): UserModel? {
        return getUserByEmailResponse()
    }

    override suspend fun getUserById(id: UUID): UserModel? {
        return getUserByIdResponse()
    }

    override suspend fun createUser(data: CreateUserData): String {
        return createUserResponse()
    }

    override suspend fun deleteUser(id: UUID) {
        return deleteUserResponse()
    }

    companion object {
        val createUserResponseDefault = "user-123"
    }
}