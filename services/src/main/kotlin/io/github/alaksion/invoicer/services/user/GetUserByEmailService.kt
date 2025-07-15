package io.github.alaksion.invoicer.services.user

import models.user.UserModel
import repository.UserRepository

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
