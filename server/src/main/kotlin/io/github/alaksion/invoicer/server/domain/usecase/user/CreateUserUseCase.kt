package io.github.alaksion.invoicer.server.domain.usecase.user

import io.github.alaksion.invoicer.server.domain.model.user.CreateUserModel

interface CreateUserUseCase {
    suspend fun create(userModel: CreateUserModel)
}

internal class CreateUserUseCaseImpl(
    private val getUserByEmailUseCase: GetUserByEmailUseCase
) : CreateUserUseCase {

    override suspend fun create(userModel: CreateUserModel) {
        TODO("Not yet implemented")
    }

}