package io.github.monolithic.invoicer.repository.datasource

import io.github.monolithic.invoicer.repository.entities.RefreshTokenEntity
import io.github.monolithic.invoicer.repository.entities.RefreshTokensTable
import kotlinx.datetime.Clock
import io.github.monolithic.invoicer.models.login.RefreshTokenModel
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.util.*

interface RefreshTokenDataSource {

    suspend fun createRefreshToken(
        token: String,
        userId: UUID
    )

    suspend fun invalidateToken(
        userId: UUID,
        token: String
    )

    suspend fun invalidateAllUserTokens(
        userId: UUID
    )

    suspend fun findUserToken(
        userId: UUID,
        token: String
    ): RefreshTokenModel?
}

internal class RefreshTokenDataSourceImpl(
    private val dateProvider: Clock
) : RefreshTokenDataSource {

    override suspend fun createRefreshToken(token: String, userId: UUID) {
        return newSuspendedTransaction {
            RefreshTokensTable.insert {
                it[refreshToken] = token
                it[user] = userId
                it[enabled] = true
                it[createdAt] = dateProvider.now()
                it[updatedAt] = dateProvider.now()
            }
        }
    }

    override suspend fun invalidateToken(
        userId: UUID,
        token: String,
    ) {
        return newSuspendedTransaction {
            RefreshTokensTable.update(
                where = {
                    (RefreshTokensTable.refreshToken eq token) and
                            (RefreshTokensTable.user eq userId)
                }
            ) {
                it[enabled] = false
                it[updatedAt] = dateProvider.now()
            }
        }
    }

    override suspend fun invalidateAllUserTokens(userId: UUID) {
        return newSuspendedTransaction {
            RefreshTokensTable.update(
                where = {
                    (RefreshTokensTable.user eq userId)
                }
            ) {
                it[enabled] = false
                it[updatedAt] = dateProvider.now()
            }
        }
    }

    override suspend fun findUserToken(userId: UUID, token: String): RefreshTokenModel? {
        return newSuspendedTransaction {
            val data = RefreshTokenEntity.Companion.find {
                (RefreshTokensTable.refreshToken eq token) and
                        (RefreshTokensTable.user eq userId)
            }.firstOrNull()

            data?.let {
                RefreshTokenModel(
                    token = it.refreshToken,
                    userId = it.user.id.value,
                    enabled = it.enabled,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            }
        }
    }
}
