package repository.api.repository

import entities.RefreshTokensTable
import models.login.RefreshTokenModel
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import utils.date.api.DateProvider
import java.util.*

interface RefreshTokenRepository {

    suspend fun createRefreshToken(
        token: String,
        userId: String
    )

    suspend fun invalidateToken(
        userId: String,
        token: String
    )

    suspend fun invalidateAllUserTokens(
        userId: String
    )

    suspend fun findUserToken(
        userId: String,
        token: String
    ): RefreshTokenModel?

}

internal class RefreshTokenRepositoryImpl(
    private val dateProvider: DateProvider
) : RefreshTokenRepository {

    override suspend fun createRefreshToken(token: String, userId: String) {
        return newSuspendedTransaction {
            RefreshTokensTable.insert {
                it[refreshToken] = token
                it[user] = UUID.fromString(userId)
                it[enabled] = true
                it[createdAt] = dateProvider.currentInstant()
                it[updatedAt] = dateProvider.currentInstant()
            }
        }
    }

    override suspend fun invalidateToken(
        userId: String,
        token: String,
    ) {
        return newSuspendedTransaction {
            RefreshTokensTable.update(
                where = {
                    (RefreshTokensTable.refreshToken eq token) and
                            (RefreshTokensTable.user eq UUID.fromString(userId))
                }
            ) {
                it[enabled] = false
                it[updatedAt] = dateProvider.currentInstant()
            }
        }
    }

    override suspend fun invalidateAllUserTokens(userId: String) {
        return newSuspendedTransaction {
            RefreshTokensTable.update(
                where = {
                    (RefreshTokensTable.user eq UUID.fromString(userId))
                }
            ) {
                it[enabled] = false
                it[updatedAt] = dateProvider.currentInstant()
            }
        }
    }

    override suspend fun findUserToken(userId: String, token: String): RefreshTokenModel? {
        return newSuspendedTransaction {
            RefreshTokensTable.selectAll().where {
                (RefreshTokensTable.refreshToken eq token) and
                        (RefreshTokensTable.user eq UUID.fromString(userId))
            }.firstOrNull()?.let {
                RefreshTokenModel(
                    userId = it[RefreshTokensTable.user].toString(),
                    token = it[RefreshTokensTable.refreshToken],
                    createdAt = it[RefreshTokensTable.createdAt],
                    updatedAt = it[RefreshTokensTable.updatedAt],
                    enabled = it[RefreshTokensTable.enabled]
                )
            }
        }
    }
}