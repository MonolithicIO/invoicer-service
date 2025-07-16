package io.github.monolithic.invoicer.services.user

import io.github.monolithic.invoicer.models.user.UserModel
import java.util.*
import io.github.monolithic.invoicer.repository.UserRepository
import io.github.monolithic.invoicer.foundation.exceptions.http.notFoundError

interface GetUserByIdService {
    suspend fun get(id: UUID): UserModel
}

internal class GetUserByIdServiceImpl(
    private val userRepository: UserRepository
) : GetUserByIdService {

    override suspend fun get(id: UUID): UserModel {
        return userRepository.getUserById(id) ?: notFoundError(message = "User not found")
    }

}
