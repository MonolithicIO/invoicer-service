package domains.user.domain.api.usecase

import domains.user.domain.api.repository.UserRepository
import utils.exceptions.notFoundError
import java.util.UUID

interface DeleteUserUseCase {
    suspend fun delete(userId: String)
}

internal class DeleteUserUseCaseImpl(
    private val userRepository: UserRepository,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : DeleteUserUseCase {
    override suspend fun delete(userId: String) {
        val user = getUserByIdUseCase.get(userId)

        if (user == null) {
            throw notFoundError("User not found")
        }

        userRepository.deleteUser(UUID.fromString(userId))
    }
}