package repository.test.repository

import kotlinx.datetime.LocalDate
import models.user.CreateUserModel
import models.user.UserModel
import repository.api.repository.UserRepository
import java.util.*

class FakeUserRepository : UserRepository {

    var getByIdResponse: suspend () -> UserModel? = { DEFAULT_USER }
    var getByEmailResponse: suspend () -> UserModel? = { DEFAULT_USER }
    var createUserResponse: suspend () -> String = { DEFAULT_USER.id.toString() }

    override suspend fun getUserByEmail(email: String): UserModel? {
        return getByEmailResponse()
    }

    override suspend fun getUserById(id: UUID): UserModel? {
        return getByIdResponse()
    }

    override suspend fun createUser(data: CreateUserModel): String {
        return createUserResponse()
    }

    override suspend fun deleteUser(id: UUID) = Unit

    companion object {
        val DEFAULT_USER = UserModel(
            id = UUID.fromString("7956749e-9d8b-4ab7-abd1-29f0b7ecb9b8"),
            password = "1234",
            verified = true,
            email = "luccab.souza@gmail.com",
            createdAt = LocalDate(2000, 6, 19),
            updatedAt = LocalDate(2000, 6, 19)
        )
    }
}