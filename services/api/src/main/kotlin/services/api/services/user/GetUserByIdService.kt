package services.api.services.user

import services.api.model.user.UserModel
import services.api.repository.UserRepository
import utils.exceptions.notFoundError
import java.util.UUID

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