package io.github.monolithic.invoicer.repository

import io.github.monolithic.invoicer.models.resetpassword.CreateResetPasswordRequestModel
import io.github.monolithic.invoicer.models.resetpassword.ResetPasswordRequestModel
import kotlinx.datetime.Instant

interface PasswordResetRepository {
    suspend fun createPasswordResetRequest(request: CreateResetPasswordRequestModel): String
    suspend fun getPasswordResetRequestByToken(token: String): ResetPasswordRequestModel?
}

internal class PasswordResetRepositoryImpl : PasswordResetRepository {

    override suspend fun createPasswordResetRequest(request: CreateResetPasswordRequestModel): String {
        return "123"
    }

    override suspend fun getPasswordResetRequestByToken(token: String): ResetPasswordRequestModel? {
        return ResetPasswordRequestModel(
            safeCode = "123",
            userId = java.util.UUID.randomUUID(),
            expiresAt = Instant.DISTANT_FUTURE,
            isConsumed = false,
            createdAt = Instant.DISTANT_PAST,
            updatedAt = Instant.DISTANT_PAST,
            expirationText = "15 minutes"
        )
    }

}