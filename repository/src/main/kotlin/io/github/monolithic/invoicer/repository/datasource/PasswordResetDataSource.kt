package io.github.monolithic.invoicer.repository.datasource

import io.github.monolithic.invoicer.models.resetpassword.CreateResetPasswordRequestModel
import io.github.monolithic.invoicer.models.resetpassword.ResetPasswordRequestModel
import io.github.monolithic.invoicer.repository.entities.ResetPasswordEntity
import io.github.monolithic.invoicer.repository.entities.ResetPasswordTable
import io.github.monolithic.invoicer.repository.mapper.toModel
import java.util.*
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

internal interface PasswordResetDataSource {
    suspend fun createPasswordResetRequest(request: CreateResetPasswordRequestModel): String
    suspend fun getPasswordResetRequestById(id: UUID): ResetPasswordRequestModel?
}

internal class PasswordResetDataSourceImpl(
    private val clock: Clock
) : PasswordResetDataSource {
    override suspend fun createPasswordResetRequest(request: CreateResetPasswordRequestModel): String {
        return newSuspendedTransaction {
            ResetPasswordTable.insertAndGetId {
                it[safeCode] = request.safeCode
                it[user] = request.userId
                it[isConsumed] = false
                it[expiresAt] = request.expiresAt
                it[expirationText] = request.expirationText
                it[createdAt] = clock.now()
                it[updatedAt] = clock.now()
            }.value.toString()
        }
    }

    override suspend fun getPasswordResetRequestById(id: UUID): ResetPasswordRequestModel? {
        return newSuspendedTransaction {
            ResetPasswordEntity
                .find { ResetPasswordTable.id eq id }
                .firstOrNull()
                ?.toModel()
        }
    }
}
