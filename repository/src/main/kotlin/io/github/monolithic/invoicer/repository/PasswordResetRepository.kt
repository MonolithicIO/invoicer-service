package io.github.monolithic.invoicer.repository

import io.github.monolithic.invoicer.models.resetpassword.CreateResetPasswordRequestModel
import io.github.monolithic.invoicer.models.resetpassword.ResetPasswordRequestModel
import io.github.monolithic.invoicer.repository.datasource.PasswordResetDataSource

interface PasswordResetRepository {
    suspend fun createPasswordResetRequest(request: CreateResetPasswordRequestModel): String
    suspend fun getPasswordResetRequestByToken(token: String): ResetPasswordRequestModel?
}

internal class PasswordResetRepositoryImpl(
    private val dataSource: PasswordResetDataSource
) : PasswordResetRepository {

    override suspend fun createPasswordResetRequest(request: CreateResetPasswordRequestModel): String {
        return dataSource.createPasswordResetRequest(request)
    }

    override suspend fun getPasswordResetRequestByToken(token: String): ResetPasswordRequestModel? {
        return dataSource.getPasswordResetRequestByToken(token)
    }
}
