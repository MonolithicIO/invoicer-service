package datasource.impl.database

import datasource.api.database.RefreshTokenDatabaseSource
import datasource.impl.entities.RefreshTokenEntity
import datasource.impl.entities.RefreshTokensTable
import kotlinx.datetime.Clock
import models.login.RefreshTokenModel
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import java.util.*

internal class RefreshTokenDatabaseSourceImpl(
    private val dateProvider: Clock
) : RefreshTokenDatabaseSource {

    override suspend fun createRefreshToken(token: String, userId: String) {
        return newSuspendedTransaction {
            RefreshTokensTable.insert {
                it[refreshToken] = token
                it[user] = UUID.fromString(userId)
                it[enabled] = true
                it[createdAt] = dateProvider.now()
                it[updatedAt] = dateProvider.now()
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
                it[updatedAt] = dateProvider.now()
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
                it[updatedAt] = dateProvider.now()
            }
        }
    }

    override suspend fun findUserToken(userId: String, token: String): RefreshTokenModel? {
        return newSuspendedTransaction {
            val data = RefreshTokenEntity.find {
                (RefreshTokensTable.refreshToken eq token) and
                        (RefreshTokensTable.user eq UUID.fromString(userId))
            }.firstOrNull()

            data?.let {
                RefreshTokenModel(
                    token = it.refreshToken,
                    userId = it.user.toString(),
                    enabled = it.enabled,
                    createdAt = it.createdAt,
                    updatedAt = it.updatedAt
                )
            }
        }
    }
}