package repository.test.repository

import models.user.CreateUserModel
import models.user.UserModel
import repository.api.repository.UserRepository
import java.util.*

class FakeUserRepository : UserRepository {

    lateinit var getByIdResponse: suspend () -> UserModel?

    override suspend fun getUserByEmail(email: String): UserModel? {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: UUID): UserModel? {
        return getByIdResponse()
    }

    override suspend fun createUser(data: CreateUserModel): String {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(id: UUID) {
        TODO("Not yet implemented")
    }
}