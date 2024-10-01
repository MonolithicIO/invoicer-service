package services.api.services.user

import services.api.repository.UserRepository

interface DeleteUserService {
    suspend fun delete(userId: String)
}

internal class DeleteUserServiceImpl(
    private val userRepository: UserRepository,
    private val getUserByIdService: GetUserByIdService
) : DeleteUserService {
    override suspend fun delete(userId: String) {
        val existingUser = getUserByIdService.get(userId)
        userRepository.deleteUser(existingUser.id)
    }
}