package services.impl.user

import repository.api.repository.UserRepository
import services.api.services.user.DeleteUserService
import services.api.services.user.GetUserByIdService

internal class DeleteUserServiceImpl(
    private val userRepository: UserRepository,
    private val getUserByIdService: GetUserByIdService
) : DeleteUserService {
    override suspend fun delete(userId: String) {
        val existingUser = getUserByIdService.get(userId)
        userRepository.deleteUser(existingUser.id)
    }
}