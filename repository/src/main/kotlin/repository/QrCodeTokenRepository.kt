package repository

import foundation.cache.CacheHandler
import kotlinx.datetime.Clock
import models.qrcodetoken.AuthorizedQrCodeToken
import models.qrcodetoken.QrCodeTokenModel
import org.jetbrains.exposed.sql.insertReturning
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.updateReturning
import repository.entities.QrCodeTokenEntity
import repository.entities.QrCodeTokenStatus
import repository.entities.QrCodeTokensTable
import repository.mapper.toModel
import java.util.*
import kotlin.time.Duration.Companion.seconds

interface QrCodeTokenRepository {
    suspend fun createQrCodeToken(
        ipAddress: String,
        agent: String,
        base64Content: String,
        content: String,
    ): QrCodeTokenModel

    suspend fun getQrCodeTokenByUUID(
        tokenId: UUID
    ): QrCodeTokenModel?

    suspend fun consumeQrCodeToken(
        tokenId: UUID
    ): QrCodeTokenModel?

    suspend fun expireQrCodeToken(
        tokenId: UUID
    )

    suspend fun getQrCodeByContentId(
        contentId: String,
    ): QrCodeTokenModel?

    suspend fun storeAuthorizedToken(
        contentId: String,
        token: AuthorizedQrCodeToken
    )

    suspend fun getAuthorizedToken(
        contentId: String
    ): AuthorizedQrCodeToken?

    suspend fun clearAuthorizedToken(
        contentId: String
    )
}

internal class QrCodeTokenRepositoryImpl(
    private val cacheHandler: CacheHandler,
    private val clock: Clock
) : QrCodeTokenRepository {

    override suspend fun createQrCodeToken(
        ipAddress: String,
        agent: String,
        base64Content: String,
        content: String,
    ): QrCodeTokenModel {
        return newSuspendedTransaction {
            QrCodeTokensTable.insertReturning {
                it[QrCodeTokensTable.ipAddress] = ipAddress
                it[QrCodeTokensTable.agent] = agent
                it[QrCodeTokensTable.base64Content] = base64Content
                it[QrCodeTokensTable.content] = content
                it[status] = QrCodeTokenStatus.GENERATED.value
                it[createdAt] = clock.now()
                it[updatedAt] = clock.now()
                it[expiresAt] = clock.now().plus(60.seconds)
            }.map {
                QrCodeTokenEntity.wrapRow(it)
            }.first().toModel()
        }
    }

    override suspend fun getQrCodeTokenByUUID(tokenId: UUID): QrCodeTokenModel? {
        return newSuspendedTransaction {
            QrCodeTokenEntity.find { QrCodeTokensTable.id eq tokenId }
                .firstOrNull()
                ?.toModel()
        }
    }

    override suspend fun consumeQrCodeToken(tokenId: UUID): QrCodeTokenModel? {
        return newSuspendedTransaction {
            QrCodeTokensTable.updateReturning(
                where = {
                    QrCodeTokensTable.id eq tokenId
                }
            ) {
                it[status] = QrCodeTokenStatus.CONSUMED.value
                it[updatedAt] = clock.now()
            }.map {
                QrCodeTokenEntity.wrapRow(it).toModel()
            }.first()
        }
    }

    override suspend fun expireQrCodeToken(tokenId: UUID) {
        return newSuspendedTransaction {
            QrCodeTokensTable.updateReturning(
                where = {
                    QrCodeTokensTable.id eq tokenId
                }
            ) {
                it[status] = QrCodeTokenStatus.EXPIRED.value
                it[updatedAt] = clock.now()
            }
        }
    }

    override suspend fun getQrCodeByContentId(contentId: String): QrCodeTokenModel? {
        return newSuspendedTransaction {
            QrCodeTokenEntity.find { QrCodeTokensTable.content eq contentId }
                .firstOrNull()
                ?.toModel()
        }
    }

    override suspend fun storeAuthorizedToken(
        contentId: String,
        token: AuthorizedQrCodeToken
    ) {
        cacheHandler.set(
            key = token.rawContent,
            value = AuthorizedQrCodeToken(
                refreshToken = token.refreshToken,
                accessToken = token.accessToken,
                rawContent = token.rawContent
            ),
            serializer = AuthorizedQrCodeToken.serializer(),
            ttlSeconds = CACHE_TTL_SECONDS
        )
    }

    override suspend fun getAuthorizedToken(contentId: String): AuthorizedQrCodeToken? {
        return cacheHandler.get(
            contentId,
            serializer = AuthorizedQrCodeToken.serializer()
        )
    }

    override suspend fun clearAuthorizedToken(contentId: String) {
        cacheHandler.delete(
            key = contentId,
        )
    }

    companion object {
        const val CACHE_TTL_SECONDS = 60L
    }
}