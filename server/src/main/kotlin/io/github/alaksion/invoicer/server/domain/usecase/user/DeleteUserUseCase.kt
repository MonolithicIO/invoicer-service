package io.github.alaksion.invoicer.server.domain.usecase.user

import io.github.alaksion.invoicer.server.domain.repository.UserRepository

interface DeleteUserUseCase {
    suspend fun delete(userId: String)
}

internal class DeleteUserUseCaseImpl(
    private val userRepository: UserRepository,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : DeleteUserUseCase {
    override suspend fun delete(userId: String) {
        val existingUser = getUserByIdUseCase.get(userId)
        userRepository.deleteUser(existingUser.id)
    }
}