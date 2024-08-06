package io.github.alaksion.invoicer.server.domain.usecase.user

import io.github.alaksion.invoicer.server.domain.model.user.UserModel
import io.github.alaksion.invoicer.server.domain.repository.UserRepository

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