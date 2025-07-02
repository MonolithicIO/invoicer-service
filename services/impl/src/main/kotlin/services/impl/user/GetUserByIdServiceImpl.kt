package services.impl.user

import models.user.UserModel
import repository.UserRepository
import services.api.services.user.GetUserByIdService
import utils.exceptions.http.notFoundError
import java.util.*

internal class GetUserByIdServiceImpl(
    private val userRepository: UserRepository
) : GetUserByIdService {

    override suspend fun get(id: UUID): UserModel {
        return userRepository.getUserById(id) ?: notFoundError(message = "User not found")
    }

}
