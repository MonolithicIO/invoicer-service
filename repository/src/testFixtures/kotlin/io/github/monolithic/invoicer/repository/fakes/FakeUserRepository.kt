package io.github.monolithic.invoicer.repository.fakes

import io.github.monolithic.invoicer.models.fixtures.userModelFixture
import io.github.monolithic.invoicer.models.user.CreateUserModel
import io.github.monolithic.invoicer.models.user.UserModel
import io.github.monolithic.invoicer.repository.UserRepository
import java.util.*

class FakeUserRepository : UserRepository {

    var getByIdResponse: suspend () -> UserModel? = { userModelFixture }
    var getByEmailResponse: suspend () -> UserModel? = { userModelFixture }
    var createUserResponse: suspend () -> String = { userModelFixture.id.toString() }

    var createUserCallStack = mutableListOf<CreateUserModel>()
    var deleteUserCallStack = mutableListOf<UUID>()

    override suspend fun getUserByEmail(email: String): UserModel? {
        return getByEmailResponse()
    }

    override suspend fun getUserById(id: UUID): UserModel? {
        return getByIdResponse()
    }

    override suspend fun createUser(data: CreateUserModel): String {
        createUserCallStack.add(data)
        return createUserResponse()
    }

    override suspend fun deleteUser(id: UUID) {
        deleteUserCallStack.add(id)
    }
}