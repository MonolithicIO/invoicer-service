package io.github.monolithic.invoicer.repository

import io.github.monolithic.invoicer.models.resetpassword.CreateResetPasswordRequestModel

interface PasswordResetRepository {
    suspend fun createPasswordResetRequest(request: CreateResetPasswordRequestModel): String
}

internal class PasswordResetRepositoryImpl : PasswordResetRepository {

    override suspend fun createPasswordResetRequest(request: CreateResetPasswordRequestModel): String {
        return "123"
    }
}