package io.github.alaksion.invoicer.server.domain.usecase.user

import io.github.alaksion.invoicer.server.domain.model.user.UserModel

interface GetUserByEmailUseCase {
    suspend fun get(id: String): UserModel?
}

internal class GetUserByEmailUseCaseImpl : GetUserByEmailUseCase {

    override suspend fun get(id: String): UserModel? {
        TODO("Not yet implemented")
    }

}