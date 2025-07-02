package services.impl.user

import repository.UserRepository
import services.api.services.user.DeleteUserService
import services.api.services.user.GetUserByIdService
import java.util.UUID

internal class DeleteUserServiceImpl(
    private val userRepository: UserRepository,
    private val getUserByIdService: GetUserByIdService
) : DeleteUserService {
    override suspend fun delete(userId: UUID) {
        val existingUser = getUserByIdService.get(userId)
        userRepository.deleteUser(existingUser.id)
    }
}
