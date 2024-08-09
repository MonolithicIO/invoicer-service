package io.github.alaksion.invoicer.server.domain.usecase.user

import io.github.alaksion.invoicer.server.domain.model.user.UserModel
import io.github.alaksion.invoicer.server.domain.repository.UserRepository
import utils.exceptions.notFoundError
import java.util.UUID

interface GetUserByIdUseCase {
    suspend fun get(id: String): UserModel
}

internal class GetUserByIdUseCaseImpl(
    private val userRepository: UserRepository
) : GetUserByIdUseCase {

    override suspend fun get(id: String): UserModel {
        return userRepository.getUserById(UUID.fromString(id)) ?: notFoundError(message = "User not found")
    }

}