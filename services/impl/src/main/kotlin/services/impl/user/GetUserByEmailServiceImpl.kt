package services.impl.user

import models.user.UserModel
import repository.UserRepository
import services.api.services.user.GetUserByEmailService

internal class GetUserByEmailServiceImpl(
    private val userRepository: UserRepository
) : GetUserByEmailService {

    override suspend fun get(email: String): UserModel? {
        return userRepository.getUserByEmail(email)
    }

}
