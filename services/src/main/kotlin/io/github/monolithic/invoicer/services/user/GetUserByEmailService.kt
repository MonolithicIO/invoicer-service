package io.github.monolithic.invoicer.services.user

import io.github.monolithic.invoicer.models.user.UserModel
import io.github.monolithic.invoicer.repository.UserRepository

interface GetUserByEmailService {
    suspend fun get(email: String): UserModel?
}

internal class GetUserByEmailServiceImpl(
    private val userRepository: UserRepository
) : GetUserByEmailService {

    override suspend fun get(email: String): UserModel? {
        return userRepository.getUserByEmail(email)
    }
}
