package io.github.alaksion.invoicer.services.user

import java.util.UUID
import repository.UserRepository

interface DeleteUserService {
    suspend fun delete(userId: UUID)
}

internal class DeleteUserServiceImpl(
    private val userRepository: UserRepository,
    private val getUserByIdService: GetUserByIdService
) : DeleteUserService {
    override suspend fun delete(userId: UUID) {
        val existingUser = getUserByIdService.get(userId)
        userRepository.deleteUser(existingUser.id)
    }
}
