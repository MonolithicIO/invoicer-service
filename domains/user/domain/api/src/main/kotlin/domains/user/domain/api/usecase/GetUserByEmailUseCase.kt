package domains.user.domain.api.usecase

import domains.user.domain.api.models.UserModel
import domains.user.domain.api.repository.UserRepository

interface GetUserByEmailUseCase {
    suspend fun get(email: String): UserModel?
}

internal class GetUserByEmailUseCaseImpl(
    private val userRepository: UserRepository
) : GetUserByEmailUseCase {

    override suspend fun get(email: String): UserModel? {
        return userRepository.getUserByEmail(email)
    }

}