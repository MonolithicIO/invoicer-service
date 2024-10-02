package services.api.services.user

import models.user.UserModel
import repository.api.repository.UserRepository

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