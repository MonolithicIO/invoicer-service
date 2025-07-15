package io.github.alaksion.invoicer.services.user

import models.user.UserModel
import java.util.*
import repository.UserRepository
import utils.exceptions.http.notFoundError

interface GetUserByIdService {
    suspend fun get(id: UUID): UserModel
}

internal class GetUserByIdServiceImpl(
    private val userRepository: UserRepository
) : GetUserByIdService {

    override suspend fun get(id: UUID): UserModel {
        return userRepository.getUserById(id) ?: notFoundError(message = "User not found")
    }

}
