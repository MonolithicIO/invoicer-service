package io.github.monolithic.invoicer.repository

import io.github.monolithic.invoicer.foundation.cache.CacheHandler
import io.github.monolithic.invoicer.models.resetpassword.CreateResetPasswordRequestModel
import io.github.monolithic.invoicer.models.resetpassword.ResetPasswordRequestModel
import io.github.monolithic.invoicer.models.resetpassword.ResetPasswordToken
import io.github.monolithic.invoicer.repository.datasource.PasswordResetDataSource
import java.util.*
import kotlin.time.Duration.Companion.minutes

interface PasswordResetRepository {
    suspend fun createPasswordResetRequest(request: CreateResetPasswordRequestModel): String
    suspend fun getPasswordResetRequestById(id: UUID): ResetPasswordRequestModel?
    suspend fun incrementPasswordResetRequestAttempts(id: UUID)
    suspend fun consumePasswordResetRequest(id: UUID)
    suspend fun storeResetToken(
        token: String,
        userId: UUID
    )

    suspend fun getResetToken(
        token: String
    ): ResetPasswordToken?
}

internal class PasswordResetRepositoryImpl(
    private val dataSource: PasswordResetDataSource,
    private val cacheHandler: CacheHandler
) : PasswordResetRepository {

    override suspend fun createPasswordResetRequest(request: CreateResetPasswordRequestModel): String {
        return dataSource.createPasswordResetRequest(request)
    }

    override suspend fun getPasswordResetRequestById(id: UUID): ResetPasswordRequestModel? {
        return dataSource.getPasswordResetRequestById(id)
    }

    override suspend fun incrementPasswordResetRequestAttempts(id: UUID) {
        dataSource.incrementPasswordResetRequestAttempts(id)
    }

    override suspend fun consumePasswordResetRequest(id: UUID) {
        dataSource.consumePasswordResetRequest(id)
    }

    override suspend fun storeResetToken(token: String, userId: UUID) {
        cacheHandler.set(
            key = "reset-password-token-$token",
            value = ResetPasswordToken(
                token = token,
                userId = userId.toString()
            ),
            ttlSeconds = 15.minutes.inWholeSeconds,
            serializer = ResetPasswordToken.serializer()
        )
    }

    override suspend fun getResetToken(token: String): ResetPasswordToken? {
        return cacheHandler.get(
            key = "reset-password-token-$token",
            serializer = ResetPasswordToken.serializer()
        )
    }
}
