package services.api.services.user

import models.user.UserModel
import repository.api.repository.UserRepository
import utils.exceptions.notFoundError
import java.util.*

interface GetUserByIdService {
    suspend fun get(id: String): UserModel
}

internal class GetUserByIdServiceImpl(
    private val userRepository: UserRepository
) : GetUserByIdService {

    override suspend fun get(id: String): UserModel {
        return userRepository.getUserById(UUID.fromString(id)) ?: notFoundError(message = "User not found")
    }

}