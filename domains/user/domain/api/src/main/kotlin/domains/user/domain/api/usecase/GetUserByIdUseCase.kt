package domains.user.domain.api.usecase

import domains.user.domain.api.models.UserModel
import domains.user.domain.api.repository.UserRepository
import java.util.UUID

interface GetUserByIdUseCase {
    suspend fun get(id: String): UserModel?
}

internal class GetUserByIdUseCaseImpl(
    private val userRepository: UserRepository
) : GetUserByIdUseCase {

    override suspend fun get(id: String): UserModel? {
        return userRepository.getUserById(UUID.fromString(id))
    }
}
